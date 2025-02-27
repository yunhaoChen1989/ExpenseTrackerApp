package ca.myscc.w0847446.expensetrackerapp

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        recycleView = findViewById(R.id.expenseList)

        var expenseList = mutableListOf(
            ExpenseItem("item1", 100.0)
        )
        recycleView.adapter = RecycleAdapter(expenseList)
        recycleView.layoutManager = LinearLayoutManager(this)
    }
}