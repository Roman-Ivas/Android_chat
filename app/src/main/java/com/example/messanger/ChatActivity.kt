package com.example.messanger

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class ChatActivity : AppCompatActivity() {
    private val LOG_TAG = "ChatActivity"
    private lateinit var messagesAdapter: MessagesAdapter
    private val messagesList = mutableListOf<Message>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val messageInput = findViewById<EditText>(R.id.messageInput)
        val sendButton = findViewById<Button>(R.id.sendButton)
        val recyclerView = findViewById<RecyclerView>(R.id.messagesList)

        messagesAdapter = MessagesAdapter(messagesList)
        recyclerView.adapter = messagesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString()
            if (messageText.isNotEmpty()) {
//                sendMessageToServer(messageText)
//                messageInput.setText("") // Clear the input field

                val message = Message(messageText)
                messagesList.add(message)
                messagesAdapter.notifyItemInserted(messagesList.size - 1)

                messageInput.setText("") // Clear the input field
                recyclerView.scrollToPosition(messagesList.size - 1) // Scroll to the bottom
            }
        }

//        getMessagesFromServer();
    }

    private fun sendMessageToServer(messageText: String) {
        RetrofitClient.instance.sendMessage(messageText, System.currentTimeMillis())
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Message sent successfully to server, add it to the local list
                        val message = Message(messageText) // Timestamp is set automatically
                        messagesList.add(message)
                        messagesAdapter.notifyItemInserted(messagesList.size - 1)
                        recyclerView.scrollToPosition(messagesList.size - 1) // Scroll to the bottom
                    } else {
                        Log.e(LOG_TAG, "Server Error: ${response.code()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e(LOG_TAG, "Network Error: ${t.message}", t)
                }
            })
    }

    private fun getMessagesFromServer() {
        RetrofitClient.instance.getMessages()
            .enqueue(object : Callback<List<Message>> {
                override fun onResponse(
                    call: Call<List<Message>>,
                    response: Response<List<Message>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            messagesList.clear()
                            messagesList.addAll(it)
                            messagesAdapter.notifyDataSetChanged()
                        }
                    } else {
                        // Handle server error
                    }
                }

                override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                    // Handle network error
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    fun clickOnLogout(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
//        test()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val toast = Toast.makeText(
                    applicationContext,
                    "Clicked: settings",
                    Toast.LENGTH_SHORT
                )
                toast.show()
                true
            }

            R.id.help -> {
                val toast = Toast.makeText(
                    applicationContext,
                    "Clicked: help", Toast.LENGTH_SHORT
                )
                toast.show()
                true
            }

            R.id.about -> {
                val toast = Toast.makeText(
                    applicationContext,
                    "Clicked: about",
                    Toast.LENGTH_SHORT
                )
                toast.show()
                //                showNotification()
                //                showDialog()
                openAlertDialogYesNo()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openAlertDialogYesNo() {
        val dialogClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val toast = Toast.makeText(
                            applicationContext,
                            "Clicked: Yes", Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                        val toast = Toast.makeText(
                            applicationContext,
                            "Clicked: No", Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }

                    DialogInterface.BUTTON_NEUTRAL -> {
                        val toast = Toast.makeText(
                            applicationContext,
                            "Clicked: Hz", Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            }
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener)
            .setNeutralButton("hz", dialogClickListener).show()
    }

    private fun test() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute { //onPreExecute()
            handler.post { //UI Thread work here
                //  tvInfo.setText("Start Test")
            }

            //Background work here
            //doInBackground
            val response = sendRequestSimple("http://messageserver/pages/getMessage.php", "")
//            val response = asyncSendRequestSimple()

            //onPostExecute
            handler.post {
                Log.i("TestFunction", "Response: $response")
//                tvInfo.setText("End Test")
//                showResults(jsonResult)
            }
        }
    }

//    private fun sendRequestSimple(urlStr: String, params: String): String {
//        val url = URL("http://messageserver/pages/getMessage.php")
//        val httpURLConnection = url.openConnection() as HttpURLConnection
//        try {
//            httpURLConnection.requestMethod = "GET"
//
//            // If you need to send a POST request with parameters
//            // httpURLConnection.doOutput = true
//            // val outputWriter = OutputStreamWriter(httpURLConnection.outputStream)
//            // outputWriter.write(params)
//            // outputWriter.flush()
//
//            val responseCode = httpURLConnection.responseCode
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                val reader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
//                val response = StringBuilder()
//                var line: String?
//                while (reader.readLine().also { line = it } != null) {
//                    response.append(line)
//                }
//                reader.close()
//                Log.i("TestFunction", "Response: $response")
//                return response.toString()
//            } else {
//                // Handle the error
//                return "Error: HTTP response code $responseCode"
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return "Error: ${e.message} ResponseCode: ${httpURLConnection.responseCode}"
//        } finally {
//            httpURLConnection.disconnect()
//        }
//    }
    private fun sendRequestSimple(urlSuffix: String, nameParam: String) {
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL("http://messageserver:80/pages/getMessage.php")
            urlConnection = url.openConnection() as HttpURLConnection
            val code = urlConnection.responseCode
            if (code != 200) {
                throw IOException("Invalid response from server: $code")
            }
            val rd = BufferedReader(
                InputStreamReader(
                    urlConnection!!.inputStream
                )
            )
            val sb = StringBuilder()
            var line: String
            while (rd.readLine().also { line = it } != null) {
                Log.i("data", line)
                sb.append(
                    """
                        $line
                        """.trimIndent()
                )
            }
            var jsonResult = sb.toString()
            Log.i("TestFunction", jsonResult)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.e("ERORR = ",  e.message.toString())
        } finally {
            urlConnection?.disconnect()
        }
    }

//    private fun asyncSendRequestSimple()= CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val url = URL("https://messageserver/pages/getMessage.php")
//
//            with(withContext(Dispatchers.IO) {
//                url.openConnection()
//            } as HttpURLConnection) {
//                requestMethod = "GET"
//
//                println("Sending GET request to URL : $url")
//                println("Response Code : $responseCode")
//
//                val response = StringBuilder()
//                inputStream.bufferedReader().use {
//                    it.lines().forEach { line ->
//                        response.append(line)
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace() // Or handle the exception as needed
//            Log.e("ERORR = ",  e.message.toString())
//        }
//    }
}