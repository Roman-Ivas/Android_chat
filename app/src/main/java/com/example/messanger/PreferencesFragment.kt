package com.example.messanger

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.messanger.services.MsgService
import com.example.messanger.services.MyService

class PreferencesFragment : Fragment() {
    private lateinit var tvServiceStatus: TextView
    private lateinit var btnStopService: Button
    private lateinit var btnStartService: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_preferences, container, false)

        // Initialize the TextView, Start and Stop Service Buttons
        tvServiceStatus = view.findViewById(R.id.tvServiceStatus)
        btnStopService = view.findViewById(R.id.btnStopService)
        btnStartService = view.findViewById(R.id.btnStartService)

        // Set the button click listener
        btnStopService.setOnClickListener {
            stopService()
        }

        btnStartService.setOnClickListener {
            startService()
        }

        //return super.onCreateView(inflater, container, savedInstanceState);
        return view
    }

    // Method to update the service status text
    fun updateServiceStatus(text: String) {
        tvServiceStatus.text = text
    }

    private fun startService() {
        // Start the service
        activity?.let {
            val serviceIntent = Intent(it, MyService::class.java) //service
            it.startService(serviceIntent)
            // Update the TextView to indicate the service has started
            updateServiceStatus("SERVICE STARTED")
        }
    }

    private fun stopService() {
        // Stop the service
        activity?.let {
            val serviceIntent = Intent(it, MyService::class.java) //service!!!
            it.stopService(serviceIntent)
            // Update the TextView to indicate the service has stopped
            updateServiceStatus("SERVICE STOPPED")
        }
    }
}