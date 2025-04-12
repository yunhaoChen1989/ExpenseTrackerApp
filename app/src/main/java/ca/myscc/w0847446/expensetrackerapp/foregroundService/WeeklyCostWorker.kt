package ca.myscc.w0847446.expensetrackerapp.foregroundService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.icu.util.Currency
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import ca.myscc.w0847446.expensetrackerapp.R
import ca.myscc.w0847446.expensetrackerapp.adapter.CurrencyAdapter
import ca.myscc.w0847446.expensetrackerapp.model.ExpenseItem
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

private const val FILE_NAME = "expenseListNew.txt"
class WeeklyCostWorker(    
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val expenseList = loadTasksFromFile()
        val totalCost = expenseList.filter { it.costAssociated }.sumOf { it.amount }

        // Show notification as this is the end of the work
        showNotification("Weekly Cost Summary", "Total Task Cost: $totalCost")
        Log.d("CostCalculationWorker", "Total cost is: $totalCost")

        return Result.success()
    }

    private fun loadTasksFromFile(): List<ExpenseItem> {
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
    }

    private fun showNotification(title: String, message: String) {
        //Same channel from before
        val channelId = "weekly_cost_channel"
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Weekly Cost Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_money)
            .setContentTitle(title)
            .setContentText(message)
            .build()

        manager.notify(3, notification)
    }
}