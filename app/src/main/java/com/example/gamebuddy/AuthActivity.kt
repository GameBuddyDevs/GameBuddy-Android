package com.example.gamebuddy

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.gamebuddy.databinding.ActivityAuthBinding
import com.example.gamebuddy.databinding.ActivityMainBinding
import com.example.gamebuddy.session.SessionEvents
import com.example.gamebuddy.util.SplashViewModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.observeOn
import timber.log.Timber

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    private lateinit var binding: ActivityAuthBinding

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        splashScreen.setKeepOnScreenCondition { splashViewModel.isLoading.value }
        setContentView(binding.root)

        collectState()
    }

    private fun collectState() {
        sessionManager.sessionState.observe(this) { state ->
            Timber.d("AuthActivityke: $state")
            displayProgressBar(state.isLoading)
            processQueue(context = this,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        sessionManager.onTriggerEvent(SessionEvents.OnRemoveHeadFromQueue)
                    }
                })

            Timber.d("authToken: ${state.authToken}, didCheckForPreviousAuthUser: ${state.didCheckForPreviousAuthUser}")

//            if (state.authToken != null /*&& state.didCheckForPreviousAuthUser*/) {
//                navMainActivity()
//            }
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