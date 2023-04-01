package com.example.gamebuddy.presentation.auth.newpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentNewPasswordBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.auth.register.RegisterEvent
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue

class NewPasswordFragment : BaseAuthFragment() {
    private val viewModel: NewPasswordViewModel by viewModels()

    private var _binding: FragmentNewPasswordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentNewPasswordBinding.inflate(inflater,container,false)
        binding.btnPassword.setOnClickListener {
            newPassword()
        }
        return binding.root
    }
    private fun newPassword(){
        viewModel.onTriggerEvent(
            NewPasswordEvent.NewPassword(
                password = "1234",
                confirmPassword = "1234",
            )
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectState()
    }
    private fun collectState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(NewPasswordEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            if (state.isNewPasswordCompleted) {
                // logine gönder
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}