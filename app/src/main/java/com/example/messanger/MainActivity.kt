package com.example.messanger

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val login = findViewById<Button>(R.id.button2)
        val registration = findViewById<Button>(R.id.button3)
        login.visibility = View.GONE
        registration.visibility = View.GONE

        Handler().postDelayed({
            if (isLoggedIn()) {
                val intent = Intent(this, ChatActivity::class.java)
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
        return false // Placeholder return value
    }
}