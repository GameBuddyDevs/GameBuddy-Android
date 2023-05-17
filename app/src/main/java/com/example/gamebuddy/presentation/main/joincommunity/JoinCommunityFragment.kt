package com.example.gamebuddy.presentation.main.joincommunity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentCommunityBinding
import com.example.gamebuddy.databinding.FragmentJoinCommunityBinding
import com.example.gamebuddy.presentation.main.community.CommunityViewModel
import com.example.gamebuddy.presentation.main.community.PostAdapter


class JoinCommunityFragment : Fragment() {

    //private val viewModel: JoinCommunityViewModel by viewModels()

    private var _binding: FragmentJoinCommunityBinding? = null
    private val binding get() = _binding!!

    private var postAdapter: PostAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentJoinCommunityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}