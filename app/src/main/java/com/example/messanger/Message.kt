package com.example.messanger

data class Message(
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
)