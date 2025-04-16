package ca.myscc.w0847446.expensetrackerapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import ca.myscc.w0847446.expensetrackerapp.interfaces.ReceiverListener

class AirplaneModeReceiver(
    val listener: ReceiverListener? = null
):BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //check if this broadcast if about airplane mode
        if(intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED){
            //check if it's on or off action
            val isModeOn = Settings.Global.getInt(
                context?.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON
            ) != 0
            var msg = ""
            if (isModeOn) {
                msg = "Airplane Mode Enabled - Sync paused"
                listener?.updateUI(true)
            } else {
                msg = "Airplane Mode Disabled - Sync resumed"
                listener?.updateUI(false)
            }
            //Show a Toast
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

        }
    }
}