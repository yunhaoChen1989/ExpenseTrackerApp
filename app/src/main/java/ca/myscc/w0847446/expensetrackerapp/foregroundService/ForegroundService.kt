package ca.myscc.w0847446.expensetrackerapp.foregroundService

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.icu.util.Currency
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.room.Room
import ca.myscc.w0847446.expensetrackerapp.R
import ca.myscc.w0847446.expensetrackerapp.adapter.CurrencyAdapter
import ca.myscc.w0847446.expensetrackerapp.dao.ExpenseItemDao
import ca.myscc.w0847446.expensetrackerapp.database.ExpenseItemDatabase

import ca.myscc.w0847446.expensetrackerapp.model.ExpenseItem
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date

/**
 * foreground service class
 * yunhao chen
 * 0847446
 * Apr 9, 25
 */
private const val FILE_NAME = "expenseListNew1.txt"
class ForegroundService: Service() {
    private lateinit var expenseItemDao: ExpenseItemDao
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val channelId = "overDueService"
    //private var notiId = 1
    override fun onCreate() {
        super.onCreate()
        // Initialize Room database
        val db = Room.databaseBuilder(
            this,
            ExpenseItemDatabase::class.java,
            "ExpenseItem" // Name of the database file
        ).build()
        expenseItemDao = db.expenseItemDao
        //create the channel
        createNotificationChannel()
        //start service
        startService()
        //check overdue and send notification
        getWholeList()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }


    private fun startService(){
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Over Due Service Running")
            .setContentText("checking for overdue tasks")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
        startForeground(1, notification.build())
        //notiId++

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "Overdue Task Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun getWholeList(){
        serviceScope.launch {
            expenseItemDao.getList().collectLatest { list->
                checkOverDue(list)
            }
        }
    }
    private fun checkOverDue(expenseList: MutableList<ExpenseItem>) {
        // Example: Insert an item
        val overdueCount = expenseList.count { compareWithCurrentDate(it.date) }
        if (overdueCount > 0) {
            //Building what the notif looks like
            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Overdue Task Reminder")
                .setContentText("You have $overdueCount overdue task(s).")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //send notification
            notificationManager.notify(2, notification)
            //notiId++

        }
    }


    fun compareWithCurrentDate(dateString: String, pattern: String = "yyyy-M-dd"): Boolean {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern(pattern)
        try {
            val parsedDate = LocalDate.parse(dateString, formatter)
            return parsedDate.isBefore(currentDate)
        } catch (e: DateTimeParseException) {
            Log.e("Foreground Service", "Invalid date format. Please use $pattern.")
            return false
        }
    }
    /*private fun loadTasksFromFile(): List<ExpenseItem> {
        val loadedList = mutableListOf<ExpenseItem>()
        try{
            val file = File(filesDir, FILE_NAME)
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
}