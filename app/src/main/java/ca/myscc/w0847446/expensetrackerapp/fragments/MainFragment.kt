package ca.myscc.w0847446.expensetrackerapp.fragments

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
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.myscc.w0847446.expensetrackerapp.data.ExpenseItem

import ca.myscc.w0847446.expensetrackerapp.activities.MainActivity
import ca.myscc.w0847446.expensetrackerapp.R
import ca.myscc.w0847446.expensetrackerapp.network.RetrofitInstance
import ca.myscc.w0847446.expensetrackerapp.views.RecycleAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Calendar

private const val FILE_NAME = "expenseList.txt"

class MainFragment : Fragment() {
    private lateinit var nameExpense: EditText
    private lateinit var amount: EditText
    private lateinit var dateInput: EditText
    private lateinit var currencySpinner: Spinner
    private lateinit var recycleView: RecyclerView
    private lateinit var submitButton: Button
    private lateinit var financialTip: Button
    private lateinit var expenseList: MutableList<ExpenseItem>
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        context= requireContext()
        recycleView = view.findViewById(R.id.expenseList)
        nameExpense = view.findViewById(R.id.expenseName)
        amount = view.findViewById(R.id.amount)
        dateInput = view.findViewById(R.id.expenseDate)
        submitButton = view.findViewById(R.id.addExpense)
        financialTip = view.findViewById(R.id.finsTips)
        currencySpinner = view.findViewById(R.id.spinner)
        // Populate currency spinner with all avail currencies
        val currencies = Currency.getAvailableCurrencies().map { it.currencyCode }.sorted()
        //set currency list into adapter
        val adapterSpinner = ArrayAdapter(context, android.R.layout.simple_spinner_item, currencies)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //show list on spinner
        currencySpinner.adapter = adapterSpinner
        //set default as cad
        val defaultIndex = currencies.indexOfFirst { it.toString() == "CAD" }
        if (defaultIndex >= 0) {
            currencySpinner.setSelection(defaultIndex)
        }
        //create the item list
        /*       expenseList = mutableListOf(
                   ExpenseItem("item1", 100.0, "2025-02-26")
               )*/
        // Load saved tasks from file
        expenseList=loadListFromFile(context)
        updateTotalExpense()
        //create the adapter with the list, pass activity too, for call update total expense back
        val adapter = RecycleAdapter(this, context, expenseList)
        recycleView.adapter = adapter//set the adapter
        recycleView.layoutManager = LinearLayoutManager(context)//show it in linear layout

        //submit button event
        submitButton.setOnClickListener {
            val name = nameExpense.text.toString().trim()
            val amt = amount.text.toString().trim()
            val date = dateInput.text.toString().trim()
            val currency = Currency.getInstance(currencySpinner.selectedItem.toString())
            //validation of all input
            if(name.isNullOrEmpty() || (amt.isNullOrEmpty() || amt.toDoubleOrNull() == null) || date.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Invalid Input", Toast.LENGTH_SHORT).show()
            }else{
                //add item to the list

                expenseList.add(ExpenseItem(name, amt.toDouble(), date, currency,0.0))
                adapter.notifyDataSetChanged()//notify change to the view
                nameExpense.setText("")
                amount.setText("")
                dateInput.setText("")
                saveListToFile(context)
            }
            updateTotalExpense()

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

        //Add HeaderFragment and FooterFragment dynamically using
        //FragmentTransaction and FragmentManager.
        //val headerFragment = HeaderFragment.newInstance()
        //val footerFragment = FooterFragment.newInstance()

        /*val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.headerFragment, headerFragment)
        transaction.add(R.id.footerFragment, footerFragment)
        transaction.commit()*/
        // Use FragmentTransaction.replace() to load or switch fragments in
        //MainActivity
        /*val transaction2 = supportFragmentManager.beginTransaction()
        transaction2.replace(R.id.headerFragment, headerFragment)
        transaction2.addToBackStack(null) // Optional: Add to back stack
        transaction2.commit()*/
        //updateTotalExpense()

        // Inflate the layout for this fragment
        fetchCurrencyList()
        return view
    }
    fun saveListToFile(context: Context){
        try{
            val json = Gson().toJson(expenseList)
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
            val listFromFile: List<ExpenseItem> = Gson().fromJson(json, type)
            loadedList.addAll(listFromFile)
        }catch (e: FileNotFoundException){
            Log.d("FileManager", e.message.toString())
        } catch (e: IOException){
            Log.d("FileManager", e.message.toString())
        }
        return loadedList
    }
    fun updateTotalExpense(){
        (activity as MainActivity).updateTotalExpense(expenseList)
    }
    fun showDetails(position: Int){
        val item = expenseList[position]
        val bundle = Bundle().apply {
            putString("name", item.name)
            putString("expenseAmount", item.amount.toString())
            putString("expenseDate", item.date)
        }
        findNavController().navigate(R.id.detailFragment, bundle)
    }

    //Function to fetch motivational quote from API
    private fun fetchCurrencyList() {
        //Coroutine to fetch quote
        lifecycleScope.launch {
            try {
                val currencies = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getCurrencyList()
                }

                if (currencies.cad !=null) {
                    Snackbar.make(requireView(),currencies.cad.toString(), Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(requireView(), "No quote found", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Snackbar.make(requireView(), "Error: ${e.message}", Snackbar.LENGTH_SHORT).show()
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