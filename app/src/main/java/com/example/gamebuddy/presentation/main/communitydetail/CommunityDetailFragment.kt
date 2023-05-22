package com.example.gamebuddy.presentation.main.communitydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentCommunityBinding
import com.example.gamebuddy.databinding.FragmentCommunityDetailBinding
import com.example.gamebuddy.presentation.main.community.CommunityViewModel
import com.example.gamebuddy.presentation.main.community.PostAdapter

class CommunityDetailFragment : Fragment() {

    //private val viewModel: CommunityDetailViewModel by viewModels()

    private var _binding: FragmentCommunityDetailBinding? = null
    private val binding get() = _binding!!

    private var postAdapter: PostAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community_detail, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}