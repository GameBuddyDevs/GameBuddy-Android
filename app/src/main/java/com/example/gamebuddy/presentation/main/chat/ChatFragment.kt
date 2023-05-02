package com.example.gamebuddy.presentation.main.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.gamebuddy.databinding.FragmentChatBinding
import com.example.gamebuddy.presentation.auth.login.LoginViewModel
import com.example.gamebuddy.presentation.auth.verify.VerifyFragmentArgs
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.EnvironmentManager
import com.example.gamebuddy.util.EnvironmentModel
import com.example.gamebuddy.websocket.WebSocketClient
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels()

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

        updateEnvironment(apiType = ApiType.APPLICATION, deploymentType = DeploymentType.PRODUCTION)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val matchedUserId = ChatFragmentArgs.fromBundle(requireArguments()).matchedUserId
        Timber.d("matchedUserId: $matchedUserId")
        viewModel.onTriggerEvent(ChatEvent.SetUserProperties(matchedUserId))
        viewModel.onTriggerEvent(ChatEvent.GetMessages(matchedUserId))

//        webSocketClient = WebSocketClient(
//            onConnectionOpened = {
//                // code to execute when the WebSocket connection is opened
//                println("WebSocket connection opened.")
//            },
//            onConnectionClosed = {
//                // code to execute when the WebSocket connection is closed
//                println("WebSocket connection closed.")
//            },
//            onMessageReceived = { message ->
//                // code to execute when a WebSocket message is received
//                println("WebSocket message received: $message")
//            }
//        )

        //connectWebSocket()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.apply {
            btnSendMsg.setOnClickListener {
                val message = editTxtMsg.text.toString()
                webSocket.send(message)
            }
            icAddFriend.setOnClickListener {
                viewModel.onTriggerEvent(ChatEvent.AddFriend(viewModel.uiState.value?.matchedUserId))
            }
        }
    }

    private fun connectWebSocket() {
        webSocket = okHttpClient.newWebSocket(createRequest(), webSocketClient)
    }

    private fun createRequest(): Request {
        val websocketURL = "http://l2.eren.wtf:4568/chat"

        return Request.Builder()
            .url(websocketURL)
            .build()
    }

    private fun updateEnvironment(
        apiType: ApiType,
        deploymentType: DeploymentType
    ) {
        val index = EnvironmentManager.environments.indexOfFirst { it.apiType == apiType }
        EnvironmentManager.environments[index] = EnvironmentModel(
            apiType = apiType,
            deploymentType = deploymentType,
            path = "application/"
        )
    }

}