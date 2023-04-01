package com.example.gamebuddy.presentation.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.gamebuddy.MainActivity
import com.example.gamebuddy.databinding.FragmentLoginBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import timber.log.Timber


class LoginFragment : BaseAuthFragment() {

    private val viewModel : LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater,container,false)

        Timber.d("Login Fragmenttt")

        binding.btnLogin.setOnClickListener {
            login()
        }

        return binding.root
    }
    private fun login(){
        viewModel.onTriggerEvent(
            LoginEvent.Login(
                email = binding.usernameContainer.editText?.text.toString(),
                password = binding.passwordContainer.editText?.text.toString(),
            )
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectState()
    }

    private fun collectState(){
        viewModel.uiState.observe(viewLifecycleOwner){ state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback{
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(LoginEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            if(state.isLoginCompleted){
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}