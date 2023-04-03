package com.example.gamebuddy.presentation.auth.verify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentRegisterBinding
import com.example.gamebuddy.databinding.FragmentVerifyBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.auth.register.RegisterEvent
import com.example.gamebuddy.presentation.auth.register.RegisterViewModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue

class VerifyFragment : BaseAuthFragment() {

    private val viewModel: VerifyViewModel by viewModels()

    private var _binding: FragmentVerifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerifyBinding.inflate(inflater, container, false)

        setClickListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fromRegister = VerifyFragmentArgs.fromBundle(requireArguments()).fromRegister
        collectState(fromRegister)
    }

    private fun collectState(fromRegister: Boolean) {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(VerifyEvent.OnRemoveHeadFromQueue)
                    }
                }
            )
            if (state.isVerifyCompleted) {
                if (fromRegister) {
                    //detailse gidicek
                } else {
                    val action =
                        VerifyFragmentDirections.actionVerifyFragmentToNewPasswordFragment()
                    findNavController().navigate(action)
                }
            }

        }

    }

    private fun setClickListeners() {
        binding.verifyButton.setOnClickListener {
            viewModel.onTriggerEvent(
                VerifyEvent.EnteredVerificationCode(
                    //verificationCode = binding.verificationCode.text.toString()
                    verificationCode = getVerificationCode()
                )
            )
        }
    }

    private fun getVerificationCode(): String {
        binding.apply {
            return verificationCode1.text.toString() +
                    verificationCode2.text.toString() +
                    verificationCode3.text.toString() +
                    verificationCode4.text.toString() +
                    verificationCode5.text.toString() +
                    verificationCode6.text.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}