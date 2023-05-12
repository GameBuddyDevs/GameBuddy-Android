package com.example.gamebuddy.presentation.main.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.model.message.Conversation
import com.example.gamebuddy.data.remote.model.socketmessage.WebSocketMessageResponse
import com.example.gamebuddy.domain.usecase.main.GetMessagesFromWebSocketUseCase
import com.example.gamebuddy.domain.usecase.main.GetMessagesUseCase
import com.example.gamebuddy.domain.usecase.main.SendFriendRequestUseCase
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.isMessageExistInQueue
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getMessagesFromWebSocketUseCase: GetMessagesFromWebSocketUseCase,
    private val authTokenDao: AuthTokenDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        connectWebSocketOverStomp()
    }

    private val _uiState: MutableLiveData<ChatState> = MutableLiveData(ChatState())
    val uiState: MutableLiveData<ChatState> get() = _uiState

    private var stompClient: StompClient? = null
    private var compositeDisposable: CompositeDisposable? = null

    private val gson = GsonBuilder().create()

    fun onTriggerEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.GetMessagesFromApi -> getMessagesFromApi(event.receiverId)
            is ChatEvent.SendMessage -> sendMessage(event.matchedUserId, event.message)
            is ChatEvent.AddFriend -> addFriend(event.matchedUserId)
            is ChatEvent.SetUserProperties -> setUserProperties(event.matchedUserId)
            is ChatEvent.OnMessageReceivedFromWebSocket -> getMessagesFromWebSocket(
                event.matchedUserId,
                event.message
            )

            ChatEvent.OnRemoveHeadFromQueue -> onRemoveHeadFromQueue()
        }
    }

    private fun sendMessage(matchedUserId: String, messageBody: String) {
        viewModelScope.launch {
            _uiState.value?.let { state ->
                val authTokenDao = authTokenDao.getAuthToken()
                val currentDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(Date())
                val conversation = """
            {
                "sender": "${authTokenDao?.account_pk}",
                "receiver": "$matchedUserId",
                "message": "$messageBody",
                "date": "$currentDate"
            }
        """.trimIndent()

                compositeDisposable?.add(
                    stompClient!!.send("/app/chat", conversation)
                        .compose(applySchedulers())
                        .subscribe(
                            {
                                Timber.d("Message sent successfully")
                                //viewModel.onTriggerEvent(ChatEvent.SendMessage(userId!!, message))
                            },
                            { throwable: Throwable? -> Timber.e("Error on send message", throwable) })
                )

                Timber.d("Send message. State: $state")
                val gson = GsonBuilder().create()
                val currentConversation = gson.fromJson(conversation, Conversation::class.java)
                Timber.d("Send message. Lastly added conversation: $currentConversation")
                _uiState.value = state.copy(messages = state.messages + currentConversation)
            }
        }
    }

    private fun getMessagesFromWebSocket(matchedUserId: String, message: String) {
        _uiState.value?.let { state ->
            if (message.isNotEmpty()) {
                getMessagesFromWebSocketUseCase.execute(matchedUserId, message)
                    .onEach { dataState ->
                        _uiState.value = state.copy(isLoading = dataState.isLoading)

                        dataState.data?.let { messageData ->
                            Timber.d("Webscoketke Message data: $messageData")
                            _uiState.value = state.copy(messages = state.messages + messageData)
                        }

                        dataState.stateMessage?.let { stateMessage ->
                            appendToMessageQueue(stateMessage)
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }

    private fun setUserProperties(matchedUserId: String) {
        _uiState.value?.let { state ->
            _uiState.value = state.copy(matchedUserId = matchedUserId)
        }
    }

    private fun addFriend(matchedUserId: String?) {
        _uiState.value?.let { state ->
            if (matchedUserId != null) {
                sendFriendRequestUseCase.execute(matchedUserId)
                    .onEach { dataState ->
                        _uiState.value = state.copy(isLoading = dataState.isLoading)

                        dataState.data?.let {
                            _uiState.value = state.copy(isFriendRequestSend = true)
                        }

                        dataState.stateMessage?.let { stateMessage ->
                            appendToMessageQueue(stateMessage)
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }

    private fun getMessagesFromApi(receiverId: String) {
        _uiState.value?.let { state ->
            getMessagesUseCase.execute(receiverId)
                .onEach { dataState ->
                    _uiState.value = state.copy(isLoading = dataState.isLoading)

                    dataState.data?.let { messageData ->
                        _uiState.value = state.copy(messages = messageData.conversations)
                    }

                    dataState.stateMessage?.let { stateMessage ->
                        appendToMessageQueue(stateMessage)
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun onRemoveHeadFromQueue() {
        _uiState.value?.let {
            try {
                val queue = it.queue
                queue.remove()
                _uiState.value = it.copy(queue = queue)
                Timber.d("Queue count after remove head: $_uiState")
            } catch (e: Exception) {
                Timber.d("Nothing to remove ${e.message}")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage) {
        _uiState.value?.let { state ->
            val queue = state.queue
            if (!stateMessage.isMessageExistInQueue(queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    Timber.d("LoginViewModel Something added to queue: ${state.queue}")
                    _uiState.value = state.copy(queue = queue)
                }
            }
        }
    }

    private fun connectWebSocketOverStomp() {
        stompClient =
            Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://91.191.173.119:4569/ws/websocket")
    }

    fun handleWebSocket() {

        viewModelScope.launch {


            stompClient?.withClientHeartbeat(5000)?.withServerHeartbeat(5000)

            resetSubscriptions()

            val dispLifecycle = stompClient?.lifecycle()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { lifecycleEvent ->
                    when (lifecycleEvent.type) {
                        LifecycleEvent.Type.OPENED -> Timber.d("Stomp connection opened")
                        LifecycleEvent.Type.ERROR -> Timber.e("Stomp connection error ${lifecycleEvent.exception}")
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
            val authTokenDao = authTokenDao
            val id = authTokenDao.getAuthToken()?.account_pk

            val disposableTopic: Disposable =
                stompClient!!.topic("/user/$id/queue/messages")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ topicMessage ->
                        Timber.d("Received " + topicMessage.payload)
                        addItem(gson.fromJson(topicMessage.payload, WebSocketMessageResponse::class.java))
                    }) { throwable -> Timber.e("Error on subscribe topic", throwable) }

            compositeDisposable?.add(disposableTopic)

            stompClient?.connect()
        }
    }

    private fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable?.dispose()
        }
        compositeDisposable = CompositeDisposable()
    }

    fun disconnectWebSocket() {
        compositeDisposable?.dispose()
        stompClient?.disconnect()
    }

    private fun applySchedulers(): CompletableTransformer? {
        return CompletableTransformer { upstream: Completable ->
            upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun addItem(webSocketMessageResponse: WebSocketMessageResponse) {
        _uiState.value?.let { state ->

            val conversation = Conversation(
                date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(Date()),
                message = webSocketMessageResponse.message,
                sender = webSocketMessageResponse.senderId,
            )

            _uiState.value = state.copy(messages = state.messages + conversation)
        }
    }

}
