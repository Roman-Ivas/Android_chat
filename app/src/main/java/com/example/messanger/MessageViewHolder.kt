package com.example.messanger

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val messageTextView: TextView = view.findViewById(R.id.message_text)

    fun bind(message: Message) {
        messageTextView.text = message.text
        // Additional binding for timestamp or sender info can be done here
    }
}