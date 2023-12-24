package com.example.messanger.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.messanger.ChatFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MsgService : Service() {
    private val TAG = "MsgService"

    companion object{
        internal val BROADCAST_ACTION = "com.itstep.messanger.servicebackbroadcastmessage"
        private val PARAM_TASK          = "task"
        private val PARAM_USERS_LIST    = "users_list"
        private val PARAM_TEXT_MSG      = "textMsg"
        private val PARAM_USER_NICK      = "userNick"

        private val TASK_MSH = 1
        private val TASK_USERS = 2

        var chatFragment : ChatFragment? = null
    }

    private var bWorkService: Boolean = true

    override fun onCreate() {
        Log.i(TAG, "Service onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.i(TAG, "Service onStartCommand $startId")

        var time = intent?.getIntExtra("time",0)
        someTask()

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
//        TODO("Return the communication channel to the service.")
        Log.i(TAG, "Service onBind")
        return null
    }

    private fun someTask() {
        Thread {
            var i:Int = 0;

            while (bWorkService) {
                val intent = Intent(BROADCAST_ACTION)
                try {
                    Thread.sleep(1000)
                    Log.i(TAG, "Check user Messages")

                    if (i == 3) {
                        Log.i(TAG, "Check users!!! - $i")

                        // сообщаем о старте задачи
                        intent.putExtra(PARAM_TASK, TASK_USERS)
                        intent.putExtra(PARAM_USERS_LIST, "Vasya - online, Olya - offlline, Kolya - online")
                        sendBroadcast(intent)
                    }

                    if (i == 5) {
                        Log.i(TAG, "Check message!!! - $i")
                        if(chatFragment != null){
                            chatFragment?.onSendMsgClick("Hallo", "Ana")
                        }
                    }

                    if (i == 6) {
                        Log.i(TAG, "Check users!!! - $i")
                        intent.putExtra(PARAM_TASK, TASK_USERS)
                        intent.putExtra(PARAM_USERS_LIST, "Vasya - online, Olya - online, Kolya - offlline")
                        sendBroadcast(intent)
                    }

                    if (i == 9) {
                        Log.i(TAG, "Check users!!! - $i")

                        // сообщаем о старте задачи
                        intent.putExtra(PARAM_TASK, TASK_USERS)
                        intent.putExtra(PARAM_USERS_LIST, "Vasya - online, Olya - offlline, Kolya - offlline")
                        sendBroadcast(intent)
                    }

                    if (i == 10) {
                        if(chatFragment != null){
                            chatFragment?.onSendMsgClick("How are you?", "Lana")
                        }
                        i = 0;
                    }

                    Log.i(TAG, "Service working!!! - $i")
                    i++;

                } catch (_: Exception) {
                }
                Log.i(TAG, "Service running")
            }
            stopSelf()
        }.start()
    }

    private fun getCurrentTimeString(): String {
        // Format the current time as a string
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }

    override fun onDestroy() {
        Log.i(TAG, "Service onDestroy")
        bWorkService = false
    }
}