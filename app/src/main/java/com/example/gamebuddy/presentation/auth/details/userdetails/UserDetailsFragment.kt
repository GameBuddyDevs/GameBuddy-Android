package com.example.gamebuddy.presentation.auth.details.userdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.databinding.FragmentUserDetailsBinding
import com.example.gamebuddy.presentation.auth.details.DetailsEvent
import com.example.gamebuddy.presentation.auth.details.DetailsViewModel


class UserDetailsFragment : BaseAuthFragment() {

    private val detailsViewModel: DetailsViewModel by activityViewModels()

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()

    }

    private fun setClickListeners() {
        binding.apply {
            btnDetailsUser.setOnClickListener {
                cacheState()
                findNavController().navigate(UserDetailsFragmentDirections.actionUserDetailsFragmentToGamesFragment())
            }
        }
    }

    private fun cacheState() {
        val age = binding.ageEditText.text.toString()
        val country = binding.ccp.selectedCountryName
        val avatar = "" //  add the logic to get the user's avatar here
        val gender = if (binding.Female.isChecked) "F" else "M"

        detailsViewModel.onTriggerEvent(DetailsEvent.OnSetAge(age = age))
        detailsViewModel.onTriggerEvent(DetailsEvent.OnSetCountry(country = country))
        detailsViewModel.onTriggerEvent(DetailsEvent.OnSetAvatar(avatar = avatar))
        detailsViewModel.onTriggerEvent(DetailsEvent.OnSetGender(gender = gender))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}