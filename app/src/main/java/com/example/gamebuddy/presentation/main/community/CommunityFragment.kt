package com.example.gamebuddy.presentation.main.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentCommunityBinding
import com.example.gamebuddy.databinding.FragmentMatchBinding
import com.example.gamebuddy.presentation.main.match.MatchViewModel

class CommunityFragment : Fragment() {

    private val viewModel: CommunityViewModel by activityViewModels()

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        return binding.root
    }

}