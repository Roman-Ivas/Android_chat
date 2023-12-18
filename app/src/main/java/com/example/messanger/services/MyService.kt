package com.example.messanger.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.Random
import kotlin.concurrent.thread

class MyService : Service() {

    private val userPollingInterval = 5000L // 5 секунд
    private val messagePollingInterval = 3000L // 3 секунди
    private var isRunning = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("MyService", "Сервіс запущений")

        // Потік для опитування користувачів
        thread(start = true) {
            while (isRunning) {
                Thread.sleep(userPollingInterval)
                val userResponse = getRandomResponse()
                Log.i("MyService", "Результат опитування користувачів: $userResponse")
            }
        }

        // Потік для перевірки нових повідомлень
        thread(start = true) {
            while (isRunning) {
                Thread.sleep(messagePollingInterval)
                val messageResponse = getRandomResponse()
                Log.i("MyService", "Наявність нових повідомлень: $messageResponse")
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun checkForMessages() {
        // Перевірка наявності нових повідомлень
        val messageReceived = Intent("com.example.messanger.NEW_MESSAGE")
        messageReceived.putExtra("extra_info", "Нове повідомлення отримано")
        sendBroadcast(messageReceived)
    }

    private fun checkUserStatus() {
        // Перевірка зміни статусу користувача
        val statusChanged = Intent("com.example.messanger.USER_STATUS_CHANGED")
        statusChanged.putExtra("extra_info", "Статус користувача змінено")
        sendBroadcast(statusChanged)
    }

    override fun onDestroy() {
        isRunning = false
        Log.i("MyService", "Сервіс зупинений")
        super.onDestroy()
    }

    private fun getRandomResponse(): String {
        return if (Random().nextBoolean()) "Так" else "Ні"
    }
}