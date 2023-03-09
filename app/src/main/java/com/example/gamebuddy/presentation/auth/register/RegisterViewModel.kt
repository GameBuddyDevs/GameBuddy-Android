package com.example.gamebuddy.presentation.auth.register

import androidx.lifecycle.ViewModel
import com.example.gamebuddy.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : ViewModel() {

    val state: MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState())

    fun onTriggerEvent(event: RegisterEvent) {
        when (event) {
            RegisterEvent.OnRemoveHeadFromQueue -> TODO()
            is RegisterEvent.OnUpdateConfirmPassword -> TODO()
            is RegisterEvent.OnUpdateEmail -> TODO()
            is RegisterEvent.OnUpdatePassword -> TODO()
            is RegisterEvent.Register -> {
                register(
                    email = event.email,
                    password = event.password,
                    confirmPassword = event.confirmPassword,
                )
            }
        }
    }

    private fun register(email: String, password: String, confirmPassword: String) {
        state.value?.let { state ->
            
        }
    }
}