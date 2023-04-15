package com.example.gamebuddy.presentation.auth.details.detailsUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.databinding.FragmentDetailsUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailsUserFragment : BaseAuthFragment() {
    private val sharedViewModel: DetailsViewModel by activityViewModels()

    private var _binding: FragmentDetailsUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsUserBinding.inflate(inflater, container, false)
        binding.apply {
            btnDetailsUser.setOnClickListener {
                detailsUser()
                findNavController().navigate(DetailsUserFragmentDirections.actionDetailsUserFragmentToGamesFragment())
            }
        }
        return binding.root
    }

    private fun detailsUser() {
        val age = binding.ageEditText.text.toString()
        val country = binding.ccp.selectedCountryName
        val avatar = "" //  add the logic to get the user's avatar here

        var gender = ""
        gender = if(binding.Female.isChecked){
            "F"
        }else{
            "M"
        }

        CoroutineScope(Dispatchers.Main).launch {
            sharedViewModel.onTriggerEvent(
                DetailsUserEvent.DetailsUser(age, country, avatar, gender)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}