package com.example.gamebuddy.presentation.main.communitydetail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentCommunityDetailBinding
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.loadImageFromUrl
import com.example.gamebuddy.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CommunityDetailFragment : Fragment(), CommunityDetailAdapter.OnClickListener {

    private val viewModel: CommunityDetailViewModel by viewModels()

    private var _binding: FragmentCommunityDetailBinding? = null
    private val binding get() = _binding!!

    private var communityDetailAdapter: CommunityDetailAdapter? = null

    private val args: CommunityDetailFragmentArgs by navArgs()

    private var name: String? = null
    private var background: String? = null
    private var avatar: String? = null
    private var description: String? = null
    private var memberCount: String? = null
    private var postCount: String? = null
    private var id: String? = null
    private var isJoined: Boolean? = null

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
        initRecyclerView()
        setClickListeners()
        collectState()
    }

    private fun initRecyclerView() {
        binding.rvCommunityDetailPosts.apply {
            layoutManager = GridLayoutManager(context, 3)
            communityDetailAdapter = CommunityDetailAdapter(this@CommunityDetailFragment)
            adapter = communityDetailAdapter
        }
    }

    private fun setClickListeners() {
        binding.apply {
            icBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnFollowState.setOnClickListener {
                viewModel.onTriggerEvent(CommunityDetailEvent.FollowCommunity(id!!))
                viewModel.onTriggerEvent(CommunityDetailEvent.GetPosts(id!!))
            }
        }
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

            setButtonFollowState(state.isFollowing)

//            if (state.isFollowing && state.posts.isNullOrEmpty()) {
//                Timber.d("collectState: get posts")
//                viewModel.onTriggerEvent(CommunityDetailEvent.GetPosts(id!!))
//            }

            communityDetailAdapter?.apply {
                submitList(state.posts)
            }
        }
    }

    private fun setButtonFollowState(isFollowing: Boolean) {
        if (isFollowing || isJoined!!) {
            binding.btnFollowState.text = context?.getString(R.string.leave)
            binding.btnFollowState.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFFFF"))
            binding.btnFollowState.setTextColor(Color.parseColor("#000000"))
        } else {
            binding.btnFollowState.text = context?.getString(R.string.join)
            binding.btnFollowState.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF4D67"))
            binding.btnFollowState.setTextColor(Color.parseColor("#FFFFFFFF"))
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
        isJoined = args.isJoined
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPostClick(postId: String) {
        Timber.d("onPostClick: $postId")
    }

}