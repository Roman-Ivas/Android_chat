package com.example.messanger

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment

class MessengerActivity : AppCompatActivity(), View.OnClickListener {
    private val LOG_TAG = "MessengerActivity"
    //    private static final int FRAGMENT_ID_USER = 1;
    //    private static final int FRAGMENT_ID_CHAT = 2;
    //    private static final int FRAGMENT_ID_PREFS = 3;
    private var usersButton: Button? = null
    private var chatButton: Button? = null

    private var currentFragment: Fragment? = null
    private var usersFragment: UserFragment? = null
    private var chatFragment: ChatFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)

        usersButton = findViewById<View>(R.id.user_btn) as Button
        chatButton = findViewById<View>(R.id.chat_btn) as Button

        usersButton!!.setOnClickListener(this)
        chatButton!!.setOnClickListener(this)

        val fragmentManager = supportFragmentManager
        usersFragment = UserFragment()
        chatFragment = ChatFragment()

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

    }

    override fun onClick(view: View?) {
//        when(view?.id){
//            R.id.user_btn-> {
//                Toast.makeText(this, "Clicked 6", Toast.LENGTH_SHORT).show()
//            }
//            R.id.chat_btn-> {
//                Toast.makeText(this, "Clicked 7", Toast.LENGTH_SHORT).show()
//            }
//        }

        //Fragment
        val fragmentManager = supportFragmentManager
        var fragment: Fragment? = null

        if (view === usersButton) {
            fragment = usersFragment
        } else if (view === chatButton) {
            fragment = chatFragment
        }
//        else if (view === preferencesButton) {
//            fragment = preferencesFragment
//        }

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
        nm.notify(4, notification)
    }
}