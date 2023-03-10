package com.example.gamebuddy.presentation.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gamebuddy.databinding.FragmentRegisterBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import kotlinx.coroutines.launch
import timber.log.Timber

class RegisterFragment : BaseAuthFragment() {

    private val viewModel: RegisterViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        Timber.d("RegisterFragment: HEREEEEEEEEEEEEEE")

        binding.btnRegister.setOnClickListener {
            signup()
        }

        return binding.root
    }

    private fun signup() {
        viewModel.onTriggerEvent(
            RegisterEvent.Register(
                email = "testke@gmail.com", /*binding.inputEmail.text.toString(),*/
                password = "123456", /*binding.inputPassword.text.toString(),*/
                confirmPassword = "123456", /*binding.inputConfirmPassword.text.toString()*/
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectState()

    }

    private fun collectState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(RegisterEvent.OnRemoveHeadFromQueue)
                    }
                }
            )
        }
    }


//    private fun collectState() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.uiState.collect { state ->
//                    Timber.d("RegisterFragment: $state")
//                    uiCommunicationListener.displayProgressBar(state.isLoading)
//                    processQueue(
//                        context = requireContext(),
//                        queue = state.queue,
//                        stateMessageCallback = object : StateMessageCallback {
//                            override fun removeMessageFromStack() {
//                                viewModel.onTriggerEvent(RegisterEvent.OnRemoveHeadFromQueue)
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}