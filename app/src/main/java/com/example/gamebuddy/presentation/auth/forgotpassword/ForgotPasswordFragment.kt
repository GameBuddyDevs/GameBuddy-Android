package com.example.gamebuddy.presentation.auth.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gamebuddy.databinding.FragmentForgotPasswordBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue


class ForgotPasswordFragment : BaseAuthFragment() {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        binding.btnPassword.setOnClickListener {
            forgotPassword()
        }
        return binding.root
    }

    private fun forgotPassword() {
        viewModel.onTriggerEvent(
            ForgotPasswordEvent.ForgotPassword(
                email = binding.emailContainer.editText?.text.toString(),
                isRegister = false,
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
                        viewModel.onTriggerEvent(ForgotPasswordEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            if (state.isForgotPasswordCompleted) {
                val action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToVerifyFragment(false)
                findNavController().navigate(action)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}