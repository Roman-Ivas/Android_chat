package com.example.messanger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ChatFragment : Fragment(){
    private lateinit var sendButton: Button
    private lateinit var mesTextView: TextView

    private var sendMessageClickListener = View.OnClickListener { onSendMsgClick() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Initialize sendButton and mesTextView
        sendButton = view.findViewById(R.id.sendChatButton)
        mesTextView = view.findViewById(R.id.messageEditText)

        // Set the click listener for the sendButton
        sendButton.setOnClickListener(sendMessageClickListener)

        return view
    }

    private fun onSendMsgClick() {
        // Output message
        val listener = activity as? OnSelectedButtonListener
        listener?.onButtonSelected("20:37", mesTextView.text.toString())
        mesTextView.text = ""
    }

    interface OnSelectedButtonListener {
        fun onButtonSelected(userID: String, messageStr: String)
    }
}