package ca.myscc.w0847446.expensetrackerapp.fragments

import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Currency
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ServiceCompat.stopForeground
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import ca.myscc.w0847446.expensetrackerapp.model.ExpenseItem

import ca.myscc.w0847446.expensetrackerapp.activities.MainActivity
import ca.myscc.w0847446.expensetrackerapp.R
import ca.myscc.w0847446.expensetrackerapp.adapter.CurrencyAdapter
import ca.myscc.w0847446.expensetrackerapp.animation.ItemAnimation
import ca.myscc.w0847446.expensetrackerapp.dao.ExpenseItemDao
import ca.myscc.w0847446.expensetrackerapp.database.ExpenseItemDatabase
import ca.myscc.w0847446.expensetrackerapp.foregroundService.ForegroundService
import ca.myscc.w0847446.expensetrackerapp.model.CurrencyInfo
import ca.myscc.w0847446.expensetrackerapp.network.RetrofitInstance
import ca.myscc.w0847446.expensetrackerapp.viewModel.BackgroundColor
import ca.myscc.w0847446.expensetrackerapp.viewModel.ExpenseListViewModel
import ca.myscc.w0847446.expensetrackerapp.views.RecycleAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Calendar

private const val FILE_NAME = "expenseListNew.txt"

class MainFragment : Fragment() {
    private val backgroundColor: BackgroundColor by activityViewModels()
    //private val expenseListViewModel: ExpenseListViewModel by activityViewModels()
    private lateinit var db: ExpenseItemDatabase
    private lateinit var expenseItemDao: ExpenseItemDao
    private lateinit var nameExpense: EditText
    private lateinit var amount: EditText
    private lateinit var dateInput: EditText
    private lateinit var currencySpinner: Spinner
    private lateinit var currencyAssociated: CheckBox
    private lateinit var convertedCostBox: EditText
    private lateinit var recycleView: RecyclerView
    private lateinit var submitButton: Button
    private lateinit var financialTip: Button
    //private lateinit var expenseList: MutableList<ExpenseItem>
    private lateinit var context: Context
    private lateinit var currencyRate: CurrencyInfo
    private lateinit var notificate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ObjectAnimator.ofFloat(view, View.ROTATION_X, 0f, 360f).apply{
            duration = 500
            start()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        context= requireContext()
        //backgroundColor = ViewModelProvider(requireActivity())[BackgroundColor::class.java]
        recycleView = view.findViewById(R.id.expenseList)
        nameExpense = view.findViewById(R.id.expenseName)
        amount = view.findViewById(R.id.amount)
        dateInput = view.findViewById(R.id.expenseDate)
        submitButton = view.findViewById(R.id.addExpense)
        financialTip = view.findViewById(R.id.finsTips)
        currencySpinner = view.findViewById(R.id.spinner)
        currencyAssociated = view.findViewById(R.id.checkBox)
        convertedCostBox = view.findViewById(R.id.convertedCostBox)
        notificate = view.findViewById(R.id.notificateButton)
        //using api fetch currency rate
        fetchCurrencyList()

        //create the adapter with the list, pass activity too, for call update total expense back
        val adapter = RecycleAdapter(this, context, mutableListOf())
        recycleView.adapter = adapter//set the adapter
        recycleView.layoutManager = LinearLayoutManager(context)//show it in linear layout
        recycleView.itemAnimator = ItemAnimation()
        // Observe data
        viewLifecycleOwner.lifecycleScope.launch {
            // Initialize Room database
            db = Room.databaseBuilder(
                context,
                ExpenseItemDatabase::class.java,
                "ExpenseItem" // Name of the database file
            ).build()
            expenseItemDao = db.expenseItemDao
            expenseItemDao.getList().collectLatest { list ->
                adapter.updateList(list)
                updateTotalExpense(list)
            }
        }
        //submit button event
        submitButton.setOnClickListener {
            val name = nameExpense.text.toString().trim()
            val amt = amount.text.toString().trim()
            val date = dateInput.text.toString().trim()
            val associated = currencyAssociated.isChecked
            //val currency =
            val associatedRate = convertedCostBox.text.toString().trim()
            //validation of all input
            if(name.isNullOrEmpty() || (amt.isNullOrEmpty() || amt.toDoubleOrNull() == null) || date.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Invalid Input", Toast.LENGTH_SHORT).show()
            }else{
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    //add item to the list
                    //expenseList.add(ExpenseItem(name, amt.toDouble(), date, Currency.getInstance(currencySpinner.selectedItem.toString()),associatedRate.toDouble(),associated))
                    expenseItemDao.addItem(
                        ExpenseItem(
                            0,
                            name,
                            amt.toDouble(),
                            date,
                            Currency.getInstance(currencySpinner.selectedItem.toString()),
                            associatedRate.toDouble(),
                            associated
                        )
                    )
                    //touch main ui method here
                    withContext(Dispatchers.Main) { // Switch back to main for UI updates

                        adapter.notifyItemInserted(adapter.itemCount)//notify change to the view
                        //change background color when user add new data to the list
                        backgroundColor.changeBackground()
                    }
                }

            }


        }
        //user click the date input edit textbox, show the date picker dialog
        dateInput.setOnClickListener {
            //get current date
            val calendar = Calendar.getInstance()
            val y = calendar.get(Calendar.YEAR)
            val m = calendar.get(Calendar.MONTH)
            val d = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker: DatePickerDialog = DatePickerDialog(context,
                //use lambda to set date to input box
                { _, y, m, d ->
                    dateInput.setText("$y-${m + 1}-$d")
                }, y, m, d//current date
            )
            //show the dialog
            datePicker.show()
        }

        //open browser for financial tips
        financialTip.setOnClickListener {
            val financialTipsUrl = "https://google.com/"
            //using action view to open the browser in the system
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(financialTipsUrl))
            startActivity(intent)
        }
        //add listener for the spinner
        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Called when an item is selected
                val selectedItem = parent?.getItemAtPosition(position).toString()
                updateAssociatedRate(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        //when user change the amount, automatically show the currency in CAD
        amount.addTextChangedListener {
            if(currencySpinner.selectedItem!=null){
                updateAssociatedRate(currencySpinner.selectedItem.toString())
            }
        }
        //start the service manually
        notificate.setOnClickListener {
            val intent = Intent(context, ForegroundService::class.java)
            //stop the service before calling the new one
            requireContext().stopService(intent)
            ContextCompat.startForegroundService(context, intent)
            Snackbar.make(requireView(), "Service starts", Snackbar.LENGTH_SHORT).show()
        }

        return view
    }
    /*fun saveListToFile(context: Context){
        try{
            val gson = GsonBuilder()
                .registerTypeAdapter(Currency::class.java, CurrencyAdapter())
                .create()
            val json = gson.toJson(expenseListViewModel.getList())
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use{ output -> output.write(json.toByteArray())}
        }catch (e: IOException){
            Log.d("fileManager", e.message.toString())
            e.printStackTrace()
        }
    }
    fun loadListFromFile(context: Context): MutableList<ExpenseItem>{
        val loadedList = mutableListOf<ExpenseItem>()
        try{
            val file = File(context.filesDir, FILE_NAME)
            if(!file.exists())return loadedList
            val json = file.readText()
            val type = object : TypeToken<List<ExpenseItem>>(){}.type
            val gson = GsonBuilder()
                .registerTypeAdapter(Currency::class.java, CurrencyAdapter())
                .create()
            val listFromFile: List<ExpenseItem> = gson.fromJson(json, type)
            loadedList.addAll(listFromFile)
        }catch (e: FileNotFoundException){
            Log.d("FileManager", e.message.toString())
        } catch (e: IOException){
            Log.d("FileManager", e.message.toString())
        }
        return loadedList
    }*/
    fun updateTotalExpense(list: MutableList<ExpenseItem>){
        (activity as MainActivity).updateTotalExpense(list)
    }
    fun deleteItem(position: Int){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            expenseItemDao.deleteItem(position)
        }
    }
    fun showDetails(position: Int){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val item = expenseItemDao.getItem(position) ?: ExpenseItem(
                0,
                "null",
                0.0,
                "2025-4-16",
                Currency.getInstance("CAD"),
                0.0,
                false
            )
            val bundle = Bundle().apply {
                putString("name", item.name)
                putDouble("expenseAmount", item.amount)
                putString("expenseDate", item.date)
                putString("currency", item.currency.currencyCode)
                putDouble("convertedCost", item.convertedCost)
            }
            //touch main ui method here
            withContext(Dispatchers.Main) { // Switch back to main for UI updates
                findNavController().navigate(R.id.detailFragment, bundle)
            }
        }
    }

    //Function to fetch cad list from API
    private fun fetchCurrencyList(){
        var currencyList = mutableListOf<Currency>()
        //Coroutine to fetch quote
        lifecycleScope.launch {
            try {
                val currencies = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getCurrencyList()
                }

                //put all data into currency list with currency object
                if (currencies.cad.isNotEmpty()) {
                    currencies.cad.forEach {(k)->
                        if(k.length==3)
                            currencyList.add(Currency.getInstance(k.uppercase()))
                    }

                    // Populate currency spinner with all avail currencies
                    //val currencies = Currency.getAvailableCurrencies().map { it.currencyCode }.sorted()
                    //set currency list into adapter
                    val adapterSpinner = ArrayAdapter(context, android.R.layout.simple_spinner_item, currencyList)
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    //show list on spinner
                    currencySpinner.adapter = adapterSpinner
                    //set default as cad
                    val defaultIndex = currencyList.indexOfFirst { it.toString() == "CAD" }
                    if (defaultIndex >= 0) {
                        currencySpinner.setSelection(defaultIndex)
                    }
                    currencyRate = currencies
                    //update the associated currency
                    updateAssociatedRate(currencySpinner.selectedItem.toString())
                    //Snackbar.make(requireView(),currencies.cad.toString(), Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(requireView(), "No cad currency found", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Snackbar.make(requireView(), "Error: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }

    }
    private fun updateAssociatedRate(currency: String){
        if (currencyAssociated?.isChecked == true) {
            val rate = currencyRate.cad[currency.lowercase()]
            val amt = if (amount.text.toString()=="") 0.0  else amount.text.toString().toDouble()
            if (rate != null) {
                convertedCostBox.setText((rate * amt).toString())
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MainFragment.
         */

        @JvmStatic
        fun newInstance(): Fragment {
            return MainFragment()
        }
    }
}