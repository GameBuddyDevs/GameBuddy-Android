package com.example.gamebuddy.presentation.main.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamebuddy.databinding.FragmentChatBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var webSocketClient: WebSocketListener
    private val okHttpClient = OkHttpClient()
    private lateinit var webSocket: WebSocket

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectWebSocket()
    }

    private fun connectWebSocket() {
        webSocket = okHttpClient.newWebSocket(createRequest(), webSocketClient)
    }

    private fun createRequest(): Request {
        val websocketURL = "wss://"

        return Request.Builder()
            .url(websocketURL)
            .build()
    }

}