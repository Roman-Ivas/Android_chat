package com.example.messanger

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.messanger.services.MsgService
import com.example.messanger.services.MyService
import java.io.IOException

class MessengerActivity : AppCompatActivity(), View.OnClickListener, ChatFragment.OnSelectedButtonListener{
    private val LOG_TAG = "MessengerActivity"
    //    private static final int FRAGMENT_ID_USER = 1;
    //    private static final int FRAGMENT_ID_CHAT = 2;
    //    private static final int FRAGMENT_ID_PREFS = 3;
    private var usersButton: Button? = null
    private var chatButton: Button? = null
    private var preferencesButton: Button? = null

    private var currentFragment: Fragment? = null
    private var usersFragment: UserFragment? = null
    private var chatFragment: ChatFragment? = null
    private var preferencesFragment: PreferencesFragment? = null

    //---------------------------------------------------
    var br: BroadcastReceiver? = null

    val BROADCAST_ACTION = "com.itstep.messanger.servicebackbroadcastmessage"

    val PARAM_TASK          = "task"
    val PARAM_USERS_LIST    = "users_list"
    val PARAM_TEXT_MSG      = "textMsg"
    val PARAM_USER_NICK     = "userNick"

    val TASK_MSH = 1
    val TASK_USERS = 2
    //---------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)

        createImage()

        usersButton = findViewById<View>(R.id.user_btn) as Button
        chatButton = findViewById<View>(R.id.chat_btn) as Button
        preferencesButton = findViewById<View>(R.id.preferences_btn) as Button

        usersButton!!.setOnClickListener(this)
        chatButton!!.setOnClickListener(this)
        preferencesButton!!.setOnClickListener(this)

        setFont(usersButton!!)

        val fragmentManager = supportFragmentManager
        usersFragment = UserFragment()
        chatFragment = ChatFragment()
        preferencesFragment = PreferencesFragment()

        // начинаем транзакцию

        // начинаем транзакцию
        val ft = fragmentManager.beginTransaction()
        // Создаем и добавляем первый фрагмент
        // Создаем и добавляем первый фрагмент
        ft.add(R.id.container, chatFragment!!, "")
        // Подтверждаем операцию
        // Подтверждаем операцию
        ft.commit()

        currentFragment = chatFragment

//        // create BroadcastReceiver
//        br = object : BroadcastReceiver() {
//            // действия при получении сообщений
//            override fun onReceive(context: Context, intent: Intent) {
////                val PARAM_TASK          = "task"
////                val PARAM_STATUS        = "status"
////                val PARAM_TEXT_MSG      = "textMsg"
//
//                val task = intent.getIntExtra(PARAM_TASK, 0)
//
//                if (task == TASK_MSH) {
//                    val msg = intent.getStringExtra(PARAM_TEXT_MSG)
//                    val user = intent.getStringExtra(PARAM_USER_NICK)
//
//                    Log.i(LOG_TAG, "$user - $msg")
//
//                    if (msg != null && user != null) {
//                        showNotification(msg, user)
//                    }
//                } else
//                    if (task == TASK_USERS) {
//                        val usersList = intent.getStringExtra(PARAM_USERS_LIST)
//
//                        Log.i(LOG_TAG, usersList!!)
//                    }
//            }
//        }

        // Initialize BroadcastReceiver
        br = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val task = intent?.getIntExtra(PARAM_TASK, 0)
                when (task) {
                    TASK_MSH -> {
                        val msg = intent.getStringExtra(PARAM_TEXT_MSG)
                        val userNick = intent.getStringExtra(PARAM_USER_NICK)
                        handleNewMessage(userNick, msg)
                    }
                    TASK_USERS -> {
                        val usersList = intent.getStringExtra(PARAM_USERS_LIST)
                        handleUsersListUpdate(usersList)
                    }
                }
            }
        }
        // Register BroadcastReceiver
        val intentFilter = IntentFilter(BROADCAST_ACTION)
//        registerReceiver(br, intentFilter)

    }

    private fun handleNewMessage(userNick: String?, msg: String?) {
        // Handle new message here
        Toast.makeText(this, "New message from $userNick: $msg", Toast.LENGTH_SHORT).show()
    }

    private fun handleUsersListUpdate(usersList: String?) {
        // Handle users list update here
        Toast.makeText(this, "Users List Updated: $usersList", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.user_btn-> {
                Toast.makeText(this, "Clicked 6", Toast.LENGTH_SHORT).show()
            }
            R.id.chat_btn-> {
                Toast.makeText(this, "Clicked 7", Toast.LENGTH_SHORT).show()
            }
            R.id.preferences_btn-> {
                Toast.makeText(this, "Clicked 8", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MyService::class.java) //service!!!
                startService(intent)

                // Send a custom broadcast
//                sendCustomBroadcast()
            }

        }

        //Fragment
        val fragmentManager = supportFragmentManager
        var fragment: Fragment? = null

        if (view === usersButton) {
            fragment = usersFragment
        } else if (view === chatButton) {
            fragment = chatFragment
        }
        else if (view === preferencesButton) {
            fragment = preferencesFragment
        }

        if (fragment == null) {
            showNotification("Unknown", "ERROR!!!")
            return
        }

        if (fragment === currentFragment) return

        currentFragment = fragment

        // Динамическое переключение на другой фрагмент
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.container, currentFragment!!, "")
        ft.addToBackStack(null)
        ft.setCustomAnimations(
            android.R.animator.fade_in, android.R.animator.fade_out
        )
        ft.commit()

        // Make sure the transaction is completed
        fragmentManager.executePendingTransactions()

        // Update TextView in PreferencesFragment
        if (fragment is PreferencesFragment) {
            (fragment as PreferencesFragment).updateServiceStatus("SERVICE STARTED")
        }
    }

    private fun sendCustomBroadcast() {
        val intent = Intent(BROADCAST_ACTION)
        // Add extra data if necessary
        intent.putExtra("extra_key", "extra_value")
        sendBroadcast(intent)
    }

    private fun showNotification(title: String, message: String) {
        //       Toast.makeText(this, "showNotification", Toast.LENGTH_SHORT).show();
        //Log.d(LOG_TAG, "showNotification")
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "channel_id_01"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "My_Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            nm.createNotificationChannel(notificationChannel)
        }
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                //    .setSmallIcon(R.drawable.ic_baseline_center_focus_weak_24) //TODO  .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_camera_alt_24))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(message)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(contentIntent)
        val notification = builder.build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        nm.notify(4, notification) //????????? error TODO
    }

    override fun onButtonSelected(userID: String, messageStr: String) {
//        showNotification(userID, messageStr)
        Toast.makeText(this, messageStr, Toast.LENGTH_SHORT).show()
    }

    private fun createImage() {
        val imageView: ImageView = findViewById(R.id.image)
        val filename = "android_001.png"
        try {
            applicationContext.assets.open(filename).use { inputStream ->
                val drawable = Drawable.createFromStream(inputStream, null)
                imageView.setImageDrawable(drawable)
                imageView.scaleType = ImageView.ScaleType.FIT_XY
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setFont(view: Button) {
        val myCustomFontBold = Typeface.createFromAsset(assets, "font/fontawesome_webfont.ttf")
        view.typeface = myCustomFontBold
    }

//    @SuppressLint("UnspecifiedRegisterReceiverFlag")
//    override fun onStart() {
//        super.onStart()
//
////        // create filter for BroadcastReceiver
////        val intFilt = IntentFilter(BROADCAST_ACTION)
////        // register BroadcastReceiver
////        try {
////            registerReceiver(br, intFilt)
////        }catch (e: Exception){
////            e.printStackTrace()
////        }
////
////        Log.i(LOG_TAG, "onStart")
//
//        // Register BroadcastReceiver here
//        br = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                val task = intent.getIntExtra(PARAM_TASK, 0)
//
//                if (task == TASK_MSH) {
//                    val msg = intent.getStringExtra(PARAM_TEXT_MSG)
//                    val user = intent.getStringExtra(PARAM_USER_NICK)
//
//                    Log.i(LOG_TAG, "$user - $msg")
//
//                    if (msg != null && user != null) {
//                        showNotification(msg, user)
//                    }
//                } else
//                    if (task == TASK_USERS) {
//                        val usersList = intent.getStringExtra(PARAM_USERS_LIST)
//
//                        Log.i(LOG_TAG, usersList!!)
//                    }
//            }
//        }
//        val intFilt = IntentFilter(BROADCAST_ACTION)
//        registerReceiver(br, intFilt)
//    }

    override fun onRestart() {
        super.onRestart()

        Log.i(LOG_TAG, "onRestart")
    }

    override fun onStop() {
        super.onStop()

        Log.i(LOG_TAG, "onStop")
//        unregisterReceiver(br)
    }
}