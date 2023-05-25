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
import com.example.gamebuddy.presentation.main.community.CommunityEvent
import com.example.gamebuddy.presentation.main.community.CommunityViewModel
import com.example.gamebuddy.presentation.main.community.PostAdapter
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.loadImageFromUrl
import com.example.gamebuddy.util.processQueue

class CommunityDetailFragment : Fragment() {

    private val viewModel: CommunityDetailViewModel by viewModels()

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
    private var id: String? = null

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
        collectState()
    }

    private fun collectState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            //loading bar
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(CommunityDetailEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            postAdapter?.apply {
                submitList(state.posts)
            }
        }
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
        viewModel.onTriggerEvent(CommunityDetailEvent.GetPosts(id!!))
    }

    private fun getArgs() {
        name = args.name
        background = args.background
        avatar = args.avatar
        description = args.description
        memberCount = args.memberCount
        postCount = args.postCount
        id = args.communityId
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}