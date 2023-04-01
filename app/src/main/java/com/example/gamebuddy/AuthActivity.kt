package com.example.gamebuddy

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.gamebuddy.databinding.ActivityMainBinding
import com.example.gamebuddy.session.SessionEvents
import com.example.gamebuddy.util.SplashViewModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.observeOn

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        splashScreen.setKeepOnScreenCondition { splashViewModel.isLoading.value }
        setContentView(binding.root)

        collectState()
    }

    private fun collectState() {
        sessionManager.sessionState.observe(this) { state ->
            displayProgressBar(state.isLoading)
            processQueue(context = this,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        sessionManager.onTriggerEvent(SessionEvents.OnRemoveHeadFromQueue)
                    }
                })

            if (state.authToken != null) {
                navMainActivity()
            }
        }
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}