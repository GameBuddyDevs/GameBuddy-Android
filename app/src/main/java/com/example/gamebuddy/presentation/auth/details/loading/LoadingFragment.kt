package com.example.gamebuddy.presentation.auth.details.loading

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gamebuddy.databinding.FragmentLoadingBinding
import com.example.gamebuddy.presentation.auth.details.DetailsEvent
import com.example.gamebuddy.presentation.auth.details.DetailsViewModel
import com.example.gamebuddy.MainActivity


class LoadingFragment : Fragment() {

    private val detailsViewModel: DetailsViewModel by activityViewModels()
    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsViewModel.onTriggerEvent(DetailsEvent.SendProfileDetail)
        detailsViewModel.detailsUiState.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                // Show the animation when loading
                binding.animationView.visibility = View.VISIBLE
            } else {
                // Hide the animation when done loading
                binding.animationView.visibility = View.GONE

                // Navigate to the home screen
                navMainActivity()
            }
        }
    }

    private fun navMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
