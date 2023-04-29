package com.example.gamebuddy.presentation.main.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentChatBinding
import com.example.gamebuddy.databinding.FragmentChatBoxBinding
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        return binding.root
    }

}