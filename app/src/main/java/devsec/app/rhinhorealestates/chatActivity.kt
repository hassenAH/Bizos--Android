package devsec.app.rhinhorealestates.ui.main.view

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import com.google.gson.GsonBuilder
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import devsec.app.RhinhoRealEstates.R
import devsec.app.rhinhorealestates.data.api.MessageRequest
import devsec.app.rhinhorealestates.data.api.MessageResponse
import devsec.app.rhinhorealestates.data.api.MessageService
import devsec.app.rhinhorealestates.utils.session.SessionPref
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {

    object RetrofitClient {
        private const val BASE_URL = "http://10.0.2.2:5000/"
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun <T> createService(serviceClass: Class<T>): T {
            return retrofit.create(serviceClass)
        }
    }

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var sessionPref: SessionPref
    private lateinit var user: HashMap<String, String>
    private lateinit var socket: Socket
    private var username: String? = null
    private var toUsername: String? = null
    private lateinit var messageList: ListView
    private lateinit var messageAdapter: MessageAdapter
    private val messageService: MessageService = RetrofitClient.createService(MessageService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val intent = intent
        sessionPref = SessionPref(this)
        user = sessionPref.getUserPref()
        messageEditText = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.send_button)
        messageList = findViewById(R.id.chat_list_view)

        username =  user.get(SessionPref.USER_ID).toString()
        toUsername = intent.getStringExtra("user")

        // Establishing socket connection
        val options = IO.Options()
        options.forceNew = true
        socket = IO.socket("http://10.0.2.2:5000/", options)
        socket.connect()
        socket.on(Socket.EVENT_CONNECT, onConnect)
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect)
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        socket.on("message", onIncomingMessage)


        val MessageR = MessageRequest(
            from = username!!,
            to =  toUsername!! ,
        )
        sendButton.setOnClickListener {
            val gson = GsonBuilder().create()
            val messageText = messageEditText.text.toString().trim()
            val handler = Handler()

            if (messageText.isNotEmpty()) {
                val datan = mapOf(
                    "from" to username,
                    "to" to toUsername,
                    "text" to messageText
                )
                val data = gson.toJson(datan)
                socket.emit("message", data)
                messageEditText.setText("")
                handler.postDelayed({
                    refreshMessages()
                }, 200)
            }

        }

        messageAdapter = MessageAdapter(this, mutableListOf(), username!!)
        messageList.adapter = messageAdapter
        GlobalScope.launch {
            val messages = messageService.getMessages(MessageR)
            withContext(Dispatchers.Main) {
                messageAdapter.addAll(messages)
                messageList.post {
                    messageList.setSelection(messageAdapter.count - 1)
                }
                messageList.requestFocus()
            }
        }
    }
    private fun refreshMessages() {
        val messageR = MessageRequest(
            from = username!! ,
            to =  toUsername!!,
        )
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val messageList = messageService.getMessages(messageR)
                messageAdapter.clear()
                messageAdapter.addAll(messageList)
                messageAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                // Handle the exception
            }
            messageList.post {
                messageList.setSelection(messageAdapter.count - 1)
            }
            messageList.requestFocus()
        }
    }


    private val onConnect = Emitter.Listener {
        Log.d("ChatActivity", "Socket connected")
    }

    private val onDisconnect = Emitter.Listener {
        Log.d("ChatActivity", "Socket disconnected")
    }

    private val onConnectError = Emitter.Listener {
        Log.d("ChatActivity", "Socket connection error")
    }

    private val onIncomingMessage = Emitter.Listener { args ->
        runOnUiThread {
            val data = args[0] as Map<*, *>
            val from = data["from"] as String
            val text = data["text"] as String
            val id = data["id"] as String // assuming you have the id available in the data
            if (from == toUsername) {
                // Display the message in UI
                val message = MessageResponse(id, from, toUsername!!, text)
                messageAdapter.add(message)
                messageList.setSelection(messageAdapter.count - 1)
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }
}
