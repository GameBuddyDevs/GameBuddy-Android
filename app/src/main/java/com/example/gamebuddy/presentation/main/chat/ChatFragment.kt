package com.example.gamebuddy.presentation.main.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.databinding.FragmentChatBinding
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.EnvironmentManager
import com.example.gamebuddy.util.EnvironmentModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.loadImageFromUrl
import com.example.gamebuddy.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader


@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels()

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var webSocketClient: WebSocketListener
    private val okHttpClient = OkHttpClient()
    private lateinit var webSocket: WebSocket

    private var chatAdapter: ChatAdapter? = null

    private var stompClient: StompClient? = null
    private var compositeDisposable: CompositeDisposable? = null

    // Args
    private var userId: String? = null
    private var username: String? = null
    private var avatarUrl: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        //updateEnvironment(apiType = ApiType.MESSAGE, deploymentType = DeploymentType.PRODUCTION)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        setUI()
        setupState()
        initRecyclerView()
        collectState()
        //createRequest()
        connectWebSocketOverStomp()
        setClickListeners()
    }

    @SuppressLint("CheckResult")
    private fun connectWebSocketOverStomp() {
        val headers: MutableList<StompHeader> = ArrayList()
        headers.add(StompHeader("Host", "l2.eren.wtf:4569"))

        resetSubscriptions()

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "http://l2.eren.wtf:4569/ws")
        stompClient?.connect(headers)

        stompClient?.lifecycle()?.subscribe { lifecycleEvent ->
            when (lifecycleEvent.getType()) {
                LifecycleEvent.Type.OPENED -> Timber.d("Stomp connection opened")
                LifecycleEvent.Type.ERROR -> Timber.e("Stomp connection error ${lifecycleEvent.exception}")
                LifecycleEvent.Type.CLOSED -> Timber.d("Stomp connection closed")
                else -> {}
            }
        }
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
                scrollToPosition()
            }

        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewChat.apply {
            layoutManager =
                LinearLayoutManager(this@ChatFragment.context, LinearLayoutManager.VERTICAL, false)
            chatAdapter = ChatAdapter(userId = userId)
            setHasFixedSize(true)
            adapter = chatAdapter
        }
    }

//    private fun connectWebSocketOverStomp() {
//
//        stompClient?.withClientHeartbeat(1000)?.withServerHeartbeat(1000)
//
//        resetSubscriptions()
//
//        val dispLifecycle = stompClient?.lifecycle()
//            ?.subscribeOn(Schedulers.io())
//            ?.observeOn(AndroidSchedulers.mainThread())
//            ?.subscribe { lifecycleEvent ->
//                when (lifecycleEvent.type) {
//                    LifecycleEvent.Type.OPENED -> Timber.d("Stomp connection opened")
//                    LifecycleEvent.Type.ERROR -> {
//                        Timber.e("Stomp connection error ${lifecycleEvent.exception}")
//                    }
//                    LifecycleEvent.Type.CLOSED -> {
//                        Timber.e("Stomp connection closed")
//                        resetSubscriptions()
//                    }
//                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> Timber.e("Stomp failed server heartbeat")
//                }
//            }
//
//        if (dispLifecycle != null) {
//            compositeDisposable?.add(dispLifecycle)
//        }
//
//        stompClient?.connect()
//    }

    private fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable?.dispose()
        }
        compositeDisposable = CompositeDisposable()
    }

    private fun createRequest() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://l2.eren.wtf:4569/ws");

//        return Request.Builder()
//            .url(websocketURL)
//            .build()
    }

    private fun scrollToPosition() {
        val adapter = binding.recyclerViewChat.adapter
        if (adapter != null && adapter.itemCount > 0) {
            Timber.d("scrolling to position: ${viewModel.uiState.value!!.messages.size}")
            binding.recyclerViewChat.smoothScrollToPosition(viewModel.uiState.value!!.messages.size)
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
        viewModel.onTriggerEvent(ChatEvent.GetMessagesFromApi(userId!!))
    }

    private fun getArgs() {
        userId = ChatFragmentArgs.fromBundle(requireArguments()).matchedUserId
        username = ChatFragmentArgs.fromBundle(requireArguments()).matchedUsername
        avatarUrl = ChatFragmentArgs.fromBundle(requireArguments()).matchedAvatar
    }

//    private fun getArgs() {
//        with(ChatFragmentArgs.fromBundle(requireArguments())) {
//            userId = matchedUserId
//            username = matchedUsername
//            avatarUrl = matchedAvatar
//        }
//    }

    private fun setClickListeners() {
        binding.apply {
            btnSendMsg.setOnClickListener {
                val message = editTxtMsg.text.toString()
                if (userId?.isNotEmpty() == true && message.isNotEmpty()) {
                    Timber.d("message: $message")
                    webSocket.send(message)
                    editTxtMsg.text.clear()
                }
            }
            icAddFriend.setOnClickListener { viewModel.onTriggerEvent(ChatEvent.AddFriend(viewModel.uiState.value?.matchedUserId)) }
            icBack.setOnClickListener { findNavController().popBackStack() }
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
            path = "messages/"
        )
    }

}