package com.example.messanger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messanger.services.MsgService

class ChatFragment : Fragment(){
    private lateinit var sendButton: Button
    private lateinit var messageEditText: EditText
    private lateinit var messagesRecyclerView: RecyclerView
    private val messagesAdapter = MessagesAdapter(mutableListOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        sendButton = view.findViewById(R.id.sendChatButton)
        messageEditText = view.findViewById(R.id.messageEditText)
        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView)

        messagesRecyclerView.layoutManager = LinearLayoutManager(context)
        messagesRecyclerView.adapter = messagesAdapter

        setFont(sendButton)

        sendButton.setOnClickListener { onSendMsgClick() }

        return view
    }

    private fun onSendMsgClick() {
        val messageText = messageEditText.text.toString()
        if (messageText.isNotEmpty()) {
            val message = Message(text = messageText)
            (messagesAdapter.messages as MutableList).add(message)
            messagesAdapter.notifyItemInserted(messagesAdapter.itemCount - 1)
            messageEditText.text.clear()
            messagesRecyclerView.scrollToPosition(messagesAdapter.itemCount - 1)
        }
    }

    interface OnSelectedButtonListener {
        fun onButtonSelected(userID: String, messageStr: String)
    }

    private fun setFont(view: Button) {
        val myCustomFontBold = Typeface.createFromAsset(activity?.assets, "font/fontawesome_webfont.ttf")
        view.typeface = myCustomFontBold
    }

    fun onSendMsgClick(message: String, user: String) {
        if (message.isNotEmpty()) {
            val message = Message(text = message)
            (messagesAdapter.messages as MutableList).add(message)
            messagesAdapter.notifyItemInserted(messagesAdapter.itemCount - 1)
            messagesRecyclerView.scrollToPosition(messagesAdapter.itemCount - 1)
        }
    }

    override fun onStart() {
        super.onStart()
        MsgService.chatFragment = this
    }

    override fun onStop() {
        super.onStop()
        MsgService.chatFragment = null
    }
}