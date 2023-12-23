package com.example.messanger

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.messanger.services.MyService
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class PreferencesFragment : Fragment() {
    private lateinit var tvServiceStatus: TextView
    private lateinit var btnStopService: Button
    private lateinit var btnStartService: Button

    private lateinit var etText: EditText  // EditText for input
    private lateinit var btnWriteText: Button  // Button for writing text to file
    private lateinit var btnReadText: Button  // Button for reading text from file
    private lateinit var btnClearText: Button

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

        etText = view.findViewById(R.id.etText)
        btnWriteText = view.findViewById(R.id.btnWriteText)
        btnReadText = view.findViewById(R.id.btnReadText)
        btnClearText = view.findViewById(R.id.btnClearText)

        // Set the button click listener
        btnStopService.setOnClickListener {
            stopService()
        }

        btnStartService.setOnClickListener {
            startService()
        }

        btnWriteText.setOnClickListener{
            writeText()
        }

        btnReadText.setOnClickListener{
            etText.setText(readFile())
        }

        btnClearText.setOnClickListener {
            clearText()
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

    private fun writeText() {
        val text = etText.text.toString()
        if (text.isNotEmpty()) {
            val file: File = File(requireActivity().filesDir, "text")
            if (!file.exists()) {
                file.mkdir()
            }
            try {
                val gpxfile = File(file, "file.txt")
                FileWriter(gpxfile).use { writer ->
                    writer.append(text)
                    Toast.makeText(requireContext(), "Saved your text", Toast.LENGTH_LONG).show()
                }
                etText.text.clear()
            } catch (e: Exception) {
            }
        }
    }

    private fun readFile(): String? {
        val file = File(requireActivity().filesDir, "text/file.txt")
        val text = StringBuilder()
        if (!file.exists()) return ""

        try {
            BufferedReader(FileReader(file)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    text.append(line)
                    text.append('\n')
                }
            }
        } catch (e: IOException) {
            // Handle IOException if necessary
        }
        return text.toString()
    }

    private fun clearText(){
        etText.text.clear()
    }
}