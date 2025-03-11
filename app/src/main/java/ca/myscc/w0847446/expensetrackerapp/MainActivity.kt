package ca.myscc.w0847446.expensetrackerapp

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

/**
 * Expense Tracker App
 * yunhao chen
 * 0847446
 * Feb 26,25
 */
class MainActivity : AppCompatActivity() {
    private lateinit var nameExpense: EditText
    private lateinit var amount: EditText
    private lateinit var dateInput: EditText
    private lateinit var recycleView: RecyclerView
    private lateinit var submitButton: Button
    private lateinit var financialTip: Button
    //val datePicker: DatePicker = DatePicker(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ExpenseTrackerLog","onCreate is called")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        recycleView = findViewById(R.id.expenseList)
        nameExpense = findViewById(R.id.expenseName)
        amount = findViewById(R.id.amount)
        dateInput = findViewById(R.id.expenseDate)
        submitButton = findViewById(R.id.addExpense)
        financialTip = findViewById(R.id.finsTips)

        //create the item list
        var expenseList = mutableListOf(
            ExpenseItem("item1", 100.0, "2025-02-26")
        )
        //create the adapter with the list
        val adapter = RecycleAdapter(this,expenseList)
        recycleView.adapter = adapter//set the adapter
        recycleView.layoutManager = LinearLayoutManager(this)//show it in linear layout

        //submit button event
        submitButton.setOnClickListener {
            val name = nameExpense.text.toString().trim()
            val amt = amount.text.toString().trim()
            val date = dateInput.text.toString().trim()
            //validation of all input
            if(name.isNullOrEmpty() || (amt.isNullOrEmpty() || amt.toDoubleOrNull() == null) || date.isNullOrEmpty()){
                Toast.makeText(this,"Invalid Input",Toast.LENGTH_SHORT).show()
            }else{
                //add item to the list

                expenseList.add(ExpenseItem(name, amt.toDouble(),date))
                adapter.notifyDataSetChanged()//notify change to the view
                nameExpense.setText("")
                amount.setText("")
                dateInput.setText("")
            }

        }
        //user click the date input edit textbox, show the date picker dialog
        dateInput.setOnClickListener {
            //get current date
            val calendar = Calendar.getInstance()
            val y = calendar.get(Calendar.YEAR)
            val m = calendar.get(Calendar.MONTH)
            val d = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker: DatePickerDialog = DatePickerDialog(this,
                //use lambda to set date to input box
                {_, y, m, d ->
                    dateInput.setText("$y-${m+1}-$d")}
                ,y,m,d//current date
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


    }

    override fun onStart() {
        super.onStart()
        Log.d("ExpenseTrackerLog","onStart is called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ExpenseTrackerLog","onPause is called")
    }
    override fun onResume() {
        super.onResume()
        Log.d("ExpenseTrackerLog","onResume is called")
    }
    override fun onStop() {
        super.onStop()
        Log.d("ExpenseTrackerLog","onStop is called")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("ExpenseTrackerLog","onDestroy is called")
    }
}