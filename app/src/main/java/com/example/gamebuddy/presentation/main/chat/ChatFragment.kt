package com.example.gamebuddy.presentation.main.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.databinding.FragmentChatBinding
import com.example.gamebuddy.presentation.auth.login.LoginEvent
import com.example.gamebuddy.presentation.auth.login.LoginViewModel
import com.example.gamebuddy.presentation.auth.verify.VerifyFragmentArgs
import com.example.gamebuddy.presentation.main.chatbox.FriendsAdapter
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.EnvironmentManager
import com.example.gamebuddy.util.EnvironmentModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.loadImageFromUrl
import com.example.gamebuddy.util.processQueue
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

    private var chatAdapter: ChatAdapter? = null

    private var userId: String? = null
    private var username: String? = null
    private var avatarUrl: String? = null

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

        getArgs()
        setUI()
        setupState()
        initRecyclerView()
        collectState()

        webSocketClient = WebSocketClient(
            onConnectionOpened = {
                // code to execute when the WebSocket connection is opened
                Timber.d("WebSocket connection opened.")
            },
            onConnectionClosed = {
                // code to execute when the WebSocket connection is closed
                Timber.d("WebSocket connection closed.")
            },
            onMessageReceived = { message ->
                // code to execute when a WebSocket message is received
                Timber.d("WebSocket message received: $message")
            }
        )

        connectWebSocket()
        setClickListeners()
    }

    private fun collectState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            //loading bar
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(ChatEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            chatAdapter?.apply {
                submitList(state.messages)
            }

        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewChat.apply {
            layoutManager = LinearLayoutManager(this@ChatFragment.context, LinearLayoutManager.VERTICAL, false)
            chatAdapter = ChatAdapter(userId = userId)
            adapter = chatAdapter
        }
    }

    private fun setUI() {
        binding.apply {
            Timber.d("username: $username avatarUrl: $avatarUrl")
            txtUsername.text = username
            imgUsernameAvatar.loadImageFromUrl(avatarUrl)
        }
    }

    private fun setupState() {
        viewModel.onTriggerEvent(ChatEvent.SetUserProperties(userId!!))
        viewModel.onTriggerEvent(ChatEvent.GetMessages(userId!!))
    }

    private fun connectWebSocket() {
        webSocket = okHttpClient.newWebSocket(createRequest(), webSocketClient)
    }

    private fun createRequest(): Request {
        val websocketURL = "http://l2.eren.wtf:4569/chat"

        return Request.Builder()
            .url(websocketURL)
            .build()
    }

    private fun getArgs() {
        with(ChatFragmentArgs.fromBundle(requireArguments())) {
            userId = matchedUserId
            username = matchedUsername
            avatarUrl = matchedAvatar
        }
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