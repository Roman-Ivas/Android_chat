package com.example.messanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast

class RegistrationActivity : AppCompatActivity() {
    private val LOG_TAG = "RegistrationActivity"
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        dbHelper = DBHelper(this)
    }

    fun clickOnCancel(view: View){
        finish()
    }

    fun clickOnRegister(view: View){
        val usernameEditText = findViewById<EditText>(R.id.username)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPassword)

        val username = usernameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if (!checkEmail(email)) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (!checkPassword(password, confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.usernameExists(username)) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.emailExists(email)) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
            return
        }

        Log.i(LOG_TAG, "USERNAME: ${username}, PASSWORD: ${password}, EMAIL: ${email}.")
        // Save the user in the database
        try {
            dbHelper.addUser(username, email, password)
            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPassword(password: String, confirmPassword: String) : Boolean{
        return password == confirmPassword
    }

    private fun checkEmail(email: String): Boolean{
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }
}