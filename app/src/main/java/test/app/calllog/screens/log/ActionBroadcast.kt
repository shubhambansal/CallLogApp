package test.app.calllog.screens.log

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

/**
 * This class is registered as an action from the notification shown to the user to listen for phone state
 */
class ActionBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null && intent.action == ServiceAction.STOP.name) {

            Intent(context, CallLogService::class.java).also {
                it.action = ServiceAction.STOP.name

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context?.startForegroundService(it)
                    return
                }
                context?.startService(it)
            }
        }
    }
}