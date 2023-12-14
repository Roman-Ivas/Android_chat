package com.example.messanger

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val messageTextView: TextView = view.findViewById(R.id.message_text)
    private val timestampTextView: TextView = view.findViewById(R.id.message_timestamp)

    fun bind(message: Message) {
        messageTextView.text = message.text
        timestampTextView.text = message.formattedTime
        // Additional binding for timestamp or sender info can be done here
    }
}