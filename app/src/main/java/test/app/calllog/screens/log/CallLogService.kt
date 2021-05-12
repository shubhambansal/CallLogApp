package test.app.calllog.screens.log

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import test.app.calllog.R
import test.app.calllog.data.CallLogRepository
import timber.log.Timber


class CallLogService : Service() {

    private lateinit var telephonyManager: TelephonyManager
    private var isServiceRunning: Boolean = false

    private var callLogRepository: CallLogRepository = get()

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Timber.d("onStartCommand")

        if (intent != null) {
            when (intent.action) {
                ServiceAction.START.name -> startService()
                ServiceAction.STOP.name -> stopService()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        Timber.d("onDestroy")
        super.onDestroy()
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    private fun startService() {

        if (isServiceRunning) return


        telephonyManager.listen(
            CallStateListener(::phoneStateCallBack),
            PhoneStateListener.LISTEN_CALL_STATE
        )

        val notification = createNotification()
        startForeground(1, notification)

        isServiceRunning = true
    }


    private fun phoneStateCallBack(state: Int, number: String?) {

        Timber.d("State = $state & PhoneNumber = $number")

        number?.let {
            when (state) {

                1, 2 -> startCall(it)
                0 -> endCall(it)
            }
        }
    }

    private fun startCall(number: String) {

        GlobalScope.launch(Dispatchers.IO) {

            try {
                callLogRepository.setCallStart(number)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun endCall(number: String) {

        GlobalScope.launch(Dispatchers.IO) {
            try {
                callLogRepository.setCallEnd(number)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }


    private fun stopService() {

        stopForeground(true)
        stopSelf()
        isServiceRunning = false
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
            val channel = NotificationChannel(
                notificationChannelId,
                "Call state listener",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description =
                    "Notify user that the call state of the phone is monitored by this app"
                it.enableLights(true)
                it.lightColor = Color.RED
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent =
            Intent(this, CallLogListActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val builder: NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) NotificationCompat.Builder(
                this,
                notificationChannelId
            ) else NotificationCompat.Builder(this)


        val cancelIntent =
            PendingIntent.getBroadcast(this, 1, Intent(this, ActionBroadcast::class.java).also {
                it.action = ServiceAction.STOP.name
            }, PendingIntent.FLAG_UPDATE_CURRENT)


        return builder
            .setContentText("CallLog app is listening your phone state")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .addAction(
                NotificationCompat.Action.Builder(
                    android.R.drawable.ic_menu_close_clear_cancel,
                    "Stop",
                    cancelIntent
                ).build()
            )
            .build()
    }

    /**
     * Listener for the phone state of the phone
     */
    private class CallStateListener(val callback: (Int, String?) -> Unit) :
        PhoneStateListener() {

        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            callback(state, phoneNumber)
        }
    }
}


enum class ServiceAction {

    START,
    STOP
}