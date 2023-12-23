package com.example.messanger

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.mindrot.jbcrypt.BCrypt

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "UserDatabase"
        private const val TABLE_USERS = "users"

        private const val KEY_ID = "_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = "CREATE TABLE $TABLE_USERS($KEY_ID INTEGER PRIMARY KEY, $KEY_USERNAME TEXT, $KEY_EMAIL TEXT, $KEY_PASSWORD TEXT)"
        db?.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(username: String, email: String, password: String) {
        val db = this.writableDatabase
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val values = ContentValues().apply {
            put(KEY_USERNAME, username)
            put(KEY_EMAIL, email)
            put(KEY_PASSWORD, hashedPassword)
        }

        db.insert(TABLE_USERS, null, values)
        db.close()
    }

    // Method to get a user by username
    fun getUser(username: String): Cursor? {
        val db = this.readableDatabase
        return db.query(TABLE_USERS, arrayOf(KEY_ID, KEY_USERNAME, KEY_EMAIL, KEY_PASSWORD),
            "$KEY_USERNAME=?", arrayOf(username), null, null, null, null)
    }

    fun usernameExists(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(KEY_ID),
            "$KEY_USERNAME = ?", arrayOf(username), null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun emailExists(email: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(KEY_ID),
            "$KEY_EMAIL = ?", arrayOf(email), null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    @SuppressLint("Range")
    fun validateUser(username: String, enteredPassword: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(KEY_PASSWORD),
            "$KEY_USERNAME = ?", arrayOf(username), null, null, null)

        if (cursor.moveToFirst()) {
            val storedPassword = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))
            cursor.close()
            return BCrypt.checkpw(enteredPassword, storedPassword)
        }
        cursor.close()
        return false
    }
}