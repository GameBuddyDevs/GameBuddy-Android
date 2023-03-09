package com.example.gamebuddy.session

import com.example.gamebuddy.data.datastore.AppDataStoreManager
import com.example.gamebuddy.domain.model.account.AuthToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
//    private val checkPreviousAuthUser: SessionEvents.CheckPreviousAuthUser,
//    private val logout: SessionEvents.Logout,
//    private val appDataStoreManager: AppDataStoreManager,
) {

    private val sessionScope = CoroutineScope(Main)

    // create a Flow of session state
    private val _sessionState: MutableStateFlow<SessionState> = MutableStateFlow(SessionState())
    val sessionState: StateFlow<SessionState> = _sessionState

    fun onTriggerEvent(event: SessionEvents) {
        when (event) {
            is SessionEvents.CheckPreviousAuthUser -> TODO()
            is SessionEvents.Login -> { login(event.authToken) }
            SessionEvents.Logout -> TODO()
            SessionEvents.OnRemoveHeadFromQueue -> TODO()
            is SessionEvents.Register -> {

            }
        }
    }

    private fun login(authToken: AuthToken) {
        _sessionState.value?.let { state ->
            _sessionState.value = state.copy(authToken = authToken)
        }
    }

}