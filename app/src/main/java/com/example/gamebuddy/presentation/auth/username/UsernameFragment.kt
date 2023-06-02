package com.example.gamebuddy.presentation.auth.username

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gamebuddy.databinding.FragmentUsernameBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue

class UsernameFragment : BaseAuthFragment() {
    private val viewModel: UsernameViewModel by viewModels()

    private var _binding: FragmentUsernameBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsernameBinding.inflate(inflater,container,false)
        binding.btnUsername.setOnClickListener {
            username()
        }
        return binding.root
    }

    private fun username() {
        viewModel.onTriggerEvent(
            UsernameEvent.Username(
                username = binding.usernameContainer.editText?.text.toString()
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
                        viewModel.onTriggerEvent(UsernameEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            if (state.isUsernameCompleted) {
                val action = UsernameFragmentDirections.actionUsernameFragmentToDetailsUserFragment()
                findNavController().navigate(action)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}