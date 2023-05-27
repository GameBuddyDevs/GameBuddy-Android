package com.example.gamebuddy.presentation.main.joincommunity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.gamebuddy.R
import com.example.gamebuddy.data.remote.model.joincommunity.Community
import com.example.gamebuddy.databinding.FragmentJoinCommunityBinding
import com.example.gamebuddy.presentation.main.community.CommunityEvent
import com.example.gamebuddy.presentation.main.community.PostAdapter
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinCommunityFragment : Fragment(), JoinCommunityAdapter.OnClickListener {

    private val viewModel: JoinCommunityViewModel by viewModels()

    private var _binding: FragmentJoinCommunityBinding? = null
    private val binding get() = _binding!!

    private var joinCommunityAdapter: JoinCommunityAdapter? = null

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

        initAdapter()
        setClickListeners()
        collectState()
    }

    private fun initAdapter() {
        binding.rvCommunityList.apply {
            layoutManager = GridLayoutManager(context, 2)
            joinCommunityAdapter = JoinCommunityAdapter(this@JoinCommunityFragment)
            adapter = joinCommunityAdapter
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
                        viewModel.onTriggerEvent(JoinCommunityEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            joinCommunityAdapter?.apply {
                submitList(state.communities)
            }
        }
    }

    private fun setClickListeners() {
        binding.apply {
            icBack.setOnClickListener { findNavController().popBackStack() }


        }
    }

    override fun onCommunityClick(community: Community) {
        val action =
            JoinCommunityFragmentDirections.actionJoinCommunityFragmentToCommunityDetailFragment(
                community.name,
                community.wallpaper,
                community.communityAvatar,
                community.description,
                community.memberCount.toString(),
                "40",
                community.communityId,
                community.isJoined
            )

        findNavController().navigate(action)
    }

    override fun onCommunityJoinClick(communityId: String) {
        viewModel.onTriggerEvent(JoinCommunityEvent.JoinCommunity(communityId))
    }

    /*
    * init {
        savedStatedHandle.get<String>("postId")?.let { postId ->
            onTriggerEvent(CommentEvent.GetComments(postId))
        }
    }
    * */

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}