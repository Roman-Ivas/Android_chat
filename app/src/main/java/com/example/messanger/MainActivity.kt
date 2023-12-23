package com.example.messanger

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var sPref: SharedPreferences? = null
    private val LOGIN = "login"
    private var PASSWORD = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val login = findViewById<Button>(R.id.button2)
        val registration = findViewById<Button>(R.id.button3)
        login.visibility = View.GONE
        registration.visibility = View.GONE

        Handler().postDelayed({
            if (isLoggedIn()) {
                val intent = Intent(this, MessengerActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                clickOnLogin(null)
            }
        }, 3000)
    }

    fun clickOnLogin(view: View?) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun clickOnRegistration(view: View?) {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isLoggedIn(): Boolean {
        // Implement logic to check if user is logged in
        // For example, check SharedPreferences, a database, etc.
        return loadTextPrefs() // Placeholder return value
    }

    private fun loadTextPrefs(): Boolean {
        sPref = getPreferences(MODE_PRIVATE)
        val loginText: String? = sPref?.getString(LOGIN, "")
        val passwordText: String? = sPref?.getString(PASSWORD, "")
        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show()
        if(!loginText.equals("") && !passwordText.equals("")){
            return true
        }
        return false
    }
}