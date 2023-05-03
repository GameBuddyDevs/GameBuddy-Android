package com.example.gamebuddy.presentation.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.gamebuddy.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater,container,false)

        val age = EditProfileFragmentArgs.fromBundle(requireArguments()).age
        val username = EditProfileFragmentArgs.fromBundle(requireArguments()).username
        val games = EditProfileFragmentArgs.fromBundle(requireArguments()).games
        val keywords = EditProfileFragmentArgs.fromBundle(requireArguments()).keywords
        val avatar = EditProfileFragmentArgs.fromBundle(requireArguments()).avatar

        binding.usernameEditText.hint = username
        binding.ageEditText.hint = age
        binding.gamesEditText.text = games
        binding.keywordEditText.text = keywords
        if (binding.profileAvatar != null) {
            Glide.with(requireContext())
                .load(avatar)
                .into(binding.profileAvatar)
        }
        return binding.root
    }
}