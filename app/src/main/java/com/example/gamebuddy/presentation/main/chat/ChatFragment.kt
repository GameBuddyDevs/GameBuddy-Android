package com.example.gamebuddy.presentation.main.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.databinding.FragmentChatBinding
import com.example.gamebuddy.domain.model.message.Message
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.EnvironmentManager
import com.example.gamebuddy.util.EnvironmentModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.loadImageFromUrl
import com.example.gamebuddy.util.processQueue
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.util.Date


@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels()

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private var chatAdapter: ChatAdapter? = null

    private var stompClient: StompClient? = null
    private var compositeDisposable: CompositeDisposable? = null

    // Args
    private var userId: String? = null
    private var username: String? = null
    private var avatarUrl: String? = null

    private val gson = GsonBuilder().create()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        //updateEnvironment(apiType = ApiType.MESSAGE, deploymentType = DeploymentType.PRODUCTION)

        viewModel.handleWebSocket()
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
        //connectWebSocketOverStomp()
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

            Timber.d("state: ${state.messages}")

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

    private fun addItem(message: Message) {
        //viewModel.onTriggerEvent(ChatEvent.SendMessage(message))
    }

    private fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable?.dispose()
        }
        compositeDisposable = CompositeDisposable()
    }

    private fun createRequest() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://l2.eren.wtf:4569/ws");
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

    private fun setClickListeners() {
        binding.apply {
            btnSendMsg.setOnClickListener {
                viewModel.onTriggerEvent(
                    ChatEvent.SendMessage(
                        matchedUserId = userId!!,
                        message = editTxtMsg.text.toString()
                    )
                )
                //sendMessage(editTxtMsg.text.toString())
            }

            icAddFriend.setOnClickListener {
                viewModel.onTriggerEvent(
                    ChatEvent.AddFriend(
                        viewModel.uiState.value?.matchedUserId
                    )
                )
            }

            icBack.setOnClickListener { findNavController().popBackStack() }
        }
    }


    private fun updateEnvironment(apiType: ApiType, deploymentType: DeploymentType) {
        val index = EnvironmentManager.environments.indexOfFirst { it.apiType == apiType }
        EnvironmentManager.environments[index] = EnvironmentModel(
            apiType = apiType,
            deploymentType = deploymentType,
            path = "messages/"
        )
    }

    private fun connectWebSocketOverStomp() {
        stompClient =
            Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://91.191.173.119:4569/ws/websocket")

        stompClient?.withClientHeartbeat(5000)?.withServerHeartbeat(5000)

        resetSubscriptions()

        val dispLifecycle = stompClient?.lifecycle()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> Timber.d("Stomp connection opened")
                    LifecycleEvent.Type.ERROR -> {
                        Timber.e("Stomp connection error ${lifecycleEvent.exception}")
                    }

                    LifecycleEvent.Type.CLOSED -> {
                        Timber.e("Stomp connection closed")
                        resetSubscriptions()
                    }

                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> Timber.e("Stomp failed server heartbeat")
                }
            }

        if (dispLifecycle != null) {
            compositeDisposable?.add(dispLifecycle)
        }

        // kaan id: c815aa8e-0899-426f-84bc-a41cdf216c9a, can id: c16049ca-844e-4897-b221-4d93e47e88b3
        // Receive greetings
        val disposableTopic: Disposable =
            stompClient!!.topic("/user/c815aa8e-0899-426f-84bc-a41cdf216c9a/queue/messages")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ topicMessage ->
                    Timber.d("Received " + topicMessage.payload)
                    addItem(gson.fromJson(topicMessage.payload, Message::class.java))
                }) { throwable -> Timber.e("Error on subscribe topic", throwable) }

        compositeDisposable?.add(disposableTopic)

        stompClient?.connect()
    }

    private fun sendMessage(msg: String) {
        val message = """
            {
                "sender": "c815aa8e-0899-426f-84bc-a41cdf216c9a",
                "receiver": "c815aa8e-0899-426f-84bc-a41cdf216c9a",
                "messageBody": "$msg",
                "date": "2023-05-05T12:00:00.000Z"
            }
        """.trimIndent()

        compositeDisposable?.add(
            stompClient!!.send("/app/chat", message)
                .compose(applySchedulers())
                .subscribe(
                    {
                        Timber.d("Message sent successfully")
                        //viewModel.onTriggerEvent(ChatEvent.SendMessage(userId!!, message))
                    },
                    { throwable: Throwable? -> Timber.e("Error on send message", throwable) })
        )
    }

    private fun applySchedulers(): CompletableTransformer? {
        return CompletableTransformer { upstream: Completable ->
            upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.disconnectWebSocket()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
