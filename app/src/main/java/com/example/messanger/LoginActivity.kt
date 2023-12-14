package com.example.messanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

class LoginActivity : AppCompatActivity() {
    private val LOG_TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun clickOnRegistration(view: View){
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun clickOnLogin(view: View){
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)

        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        Log.i(LOG_TAG, "USERNAME: ${username}, PASSWORD: ${password}.")
        val intent = Intent(this, MessengerActivity::class.java)
        startActivity(intent)
        finish()
    }
}