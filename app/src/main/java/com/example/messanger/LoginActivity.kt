package com.example.messanger

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    var sPref: SharedPreferences? = null
    val LOGIN = "login"
    var PASSWORD = "password"
    private val LOG_TAG = "LoginActivity"
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dbHelper = DBHelper(this)
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

        if (dbHelper.validateUser(username, password)) {
            saveLogin(username, password)
            val intent = Intent(this, MessengerActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveLogin(username: String, password: String) {
        sPref = getPreferences(MODE_PRIVATE)
        val ed: SharedPreferences.Editor? = sPref?.edit()
        ed?.putString(LOGIN, username)
        ed?.putString(PASSWORD, password)
        ed?.apply()
        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show()
    }
}