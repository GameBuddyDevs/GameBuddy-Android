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
import com.example.gamebuddy.databinding.FragmentJoinCommunityBinding
import com.example.gamebuddy.presentation.main.community.CommunityEvent
import com.example.gamebuddy.presentation.main.community.CommunityViewModel
import com.example.gamebuddy.presentation.main.community.PostAdapter
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.loadImageFromUrl
import com.example.gamebuddy.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
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
        _binding = FragmentCommunityDetailBinding.inflate(inflater, container, false)

        return binding.root
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
        background = args.background ?: "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/community-banner%2Fcs-banner.jpg?alt=media&token=9b3d9942-4be6-47e1-82b1-7eef0372515b"
        avatar = args.avatar ?: "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fminecraft6.jpg?alt=media&token=c5a6b3d9-a741-4511-a96f-1dbb8b84c1d7"
        description = args.description ?: ""
        memberCount = args.memberCount
        postCount = args.postCount
        id = args.communityId
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}