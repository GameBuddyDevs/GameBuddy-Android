package com.example.gamebuddy.session

import androidx.compose.runtime.MutableState
import com.example.gamebuddy.data.datastore.AppDataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val checkPreviousAuthUser: SessionEvents.CheckPreviousAuthUser,
    private val logout: SessionEvents.Logout,
    private val appDataStoreManager: AppDataStoreManager,
) {

    private val sessionScope = CoroutineScope(Main)

    // create a Flow of session state
    private val _sessionState: MutableStateFlow<SessionState> = MutableStateFlow(SessionState())
    val sessionState: StateFlow<SessionState> = _sessionState


}