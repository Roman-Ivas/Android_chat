package com.example.messanger

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
){
    val formattedTime: String
        get() {
            val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }
}