package com.example.gamebuddy.presentation.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gamebuddy.MainActivity
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentEditLoadingBinding
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import timber.log.Timber

class EditLoadingFragment : Fragment() {

    private val viewModel: EditProfileViewModel by activityViewModels()
    private var _binding: FragmentEditLoadingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditLoadingBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectState()
    }
    private fun collectState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        Timber.d("removeMessageFromStack: ")
                        viewModel.onTriggerEvent(EditEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            if (!state.isProfileSetupDone) {
                binding.animationView.visibility = View.VISIBLE
                val mainActivity = activity as MainActivity
                val bottomBar = mainActivity.findViewById<View>(R.id.bottom_navigation_view)
                bottomBar.visibility = View.GONE
            } else {
                binding.animationView.visibility = View.GONE
                val mainActivity = activity as MainActivity
                val bottomBar = mainActivity.findViewById<View>(R.id.bottom_navigation_view)
                bottomBar.visibility = View.VISIBLE
                findNavController().popBackStack(R.id.profileFragment,true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}