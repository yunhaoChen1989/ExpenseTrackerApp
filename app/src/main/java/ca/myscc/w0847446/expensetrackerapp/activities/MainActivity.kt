package ca.myscc.w0847446.expensetrackerapp.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ca.myscc.w0847446.expensetrackerapp.model.ExpenseItem
import ca.myscc.w0847446.expensetrackerapp.R
import ca.myscc.w0847446.expensetrackerapp.foregroundService.ForegroundService
import ca.myscc.w0847446.expensetrackerapp.foregroundService.WeeklyCostWorker
import ca.myscc.w0847446.expensetrackerapp.fragments.FooterFragment
import java.util.concurrent.TimeUnit

/**
 * Expense Tracker App
 * yunhao chen
 * 0847446
 * Feb 26,25
 */
//private const val FILE_NAME = "expenseList.txt"

class MainActivity : AppCompatActivity() {
/*    private lateinit var nameExpense: EditText
    private lateinit var amount: EditText
    private lateinit var dateInput: EditText
    private lateinit var recycleView: RecyclerView
    private lateinit var submitButton: Button
    private lateinit var financialTip: Button
    private lateinit var expenseList: MutableList<ExpenseItem>*/
    //val datePicker: DatePicker = DatePicker(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ExpenseTrackerLog", "onCreate is called")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //Request notification channel
        requestNotificationPermission()
        //send the foreground notification of overdue
        val intent = Intent(this, ForegroundService::class.java)
        ContextCompat.startForegroundService(this, intent)
        //weekly cost worker
        val periodicWorkRequest = PeriodicWorkRequestBuilder<WeeklyCostWorker>(
            7, // Repeat interval
            TimeUnit.DAYS
        ).build()
        //start the thread
        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)

    }
    //Helper for notification channel - Easier to put it here as main activity is a certainty
    //as mainFragment might not happen
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }
    fun updateTotalExpense(expenseList: List<ExpenseItem>){
        val footer = supportFragmentManager.findFragmentById(R.id.footerFragment) as FooterFragment?
        footer?.updateTotalExpensesDisplay(expenseList.sumOf { it.amount })
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