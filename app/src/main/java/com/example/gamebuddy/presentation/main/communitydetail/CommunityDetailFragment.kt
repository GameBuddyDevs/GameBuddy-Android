package com.example.gamebuddy.presentation.main.communitydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentCommunityBinding
import com.example.gamebuddy.databinding.FragmentCommunityDetailBinding
import com.example.gamebuddy.presentation.main.community.CommunityViewModel
import com.example.gamebuddy.presentation.main.community.PostAdapter
import com.example.gamebuddy.util.loadImageFromUrl

class CommunityDetailFragment : Fragment() {

    //private val viewModel: CommunityDetailViewModel by viewModels()

    private var _binding: FragmentCommunityDetailBinding? = null
    private val binding get() = _binding!!

    private var postAdapter: PostAdapter? = null

    private val args: CommunityDetailFragmentArgs by navArgs()

    private var name: String? = null
    private var background: String? = null
    private var avatar: String? = null
    private var description: String? = null
    private var memberCount: String? = null
    private var postCount: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        setUI()
    }

    private fun setUI() {
        binding.apply {
            txtUsername.text = name
            imgCommunityBackground.loadImageFromUrl(background)
            imgCommunityAvatar.loadImageFromUrl(avatar)
            txtCommunityDescription.text = description
            txtMemberCount.text = memberCount
            txtPostCount.text = postCount
        }
    }

    private fun getArgs() {
        name = args.name
        background = args.background
        avatar = args.avatar
        description = args.description
        memberCount = args.memberCount
        postCount = args.postCount
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}