package ca.myscc.w0847446.expensetrackerapp

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var nameExpense: EditText
    private lateinit var amount: EditText
    private lateinit var date: DatePicker
    private lateinit var recycleView: RecyclerView
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        recycleView = findViewById(R.id.expenseList)
        nameExpense = findViewById(R.id.expenseName)
        amount = findViewById(R.id.amount)
        //date = findViewById(R.id.expenseDate)
        submitButton = findViewById(R.id.addExpense)

        var expenseList = mutableListOf(
            ExpenseItem("item1", 100.0)
        )
        val adapter = RecycleAdapter(expenseList)
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(this)

        submitButton.setOnClickListener {
            expenseList.add(ExpenseItem(nameExpense.text.toString(), amount.text.toString().toDouble()))
            adapter.notifyDataSetChanged()
        }
    }
}