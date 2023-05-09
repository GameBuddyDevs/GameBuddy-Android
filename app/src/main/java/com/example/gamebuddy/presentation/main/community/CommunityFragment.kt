package com.example.gamebuddy.presentation.main.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gamebuddy.databinding.FragmentCommunityBinding
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue

class CommunityFragment : Fragment(), PostAdapter.OnClickListener {

    private val viewModel: CommunityViewModel by viewModels()

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private var postAdapter: PostAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        initAdapter()
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
                        viewModel.onTriggerEvent(CommunityEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            postAdapter?.apply {
                submitList(state.posts)
            }
        }
    }

    private fun initAdapter() {
        binding.rvCommunityPosts.apply {
            postAdapter = PostAdapter(this@CommunityFragment)
            adapter = postAdapter
        }
    }

    private fun setClickListeners() {
        binding.apply {
            icCreatePost.setOnClickListener {
                //findNavController().navigate(R.id.action_communityFragment_to_createPostFragment)
            }
        }
    }

    override fun onLikePostClick(postId: String) {
        viewModel.onTriggerEvent(CommunityEvent.LikePost(postId))
    }

    override fun onCommentClick(postId: String) {
        findNavController().navigate(
            CommunityFragmentDirections.actionCommunityFragmentToCommentFragment(postId)
        )
    }

}