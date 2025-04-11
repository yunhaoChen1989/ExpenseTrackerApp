package ca.myscc.w0847446.expensetrackerapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BatteryLowReceiver: BroadcastReceiver() {
    //override onReceive to receive the action
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_BATTERY_LOW){
            //Show a Toast
            Toast.makeText(context, "Sync paused due to low battery", Toast.LENGTH_LONG).show()
            Log.d("LowBattery", "Low battery")
        }
    }
}