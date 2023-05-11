package com.example.gamebuddy.presentation.main.comment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentChatBoxBinding
import com.example.gamebuddy.databinding.FragmentCommentBinding
import com.example.gamebuddy.presentation.main.chatbox.ChatBoxAdapter
import com.example.gamebuddy.presentation.main.chatbox.ChatBoxViewModel
import com.example.gamebuddy.presentation.main.chatbox.FriendsAdapter
import com.example.gamebuddy.presentation.main.community.CommunityEvent
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import org.w3c.dom.Comment

class CommentFragment : Fragment(), CommentAdapter.OnClickListener {

    private var commentAdapter: CommentAdapter? = null

    private val viewModel: CommentViewModel by viewModels()

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)

        //updateEnvironment(apiType = ApiType.APPLICATION, deploymentType = DeploymentType.PRODUCTION)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        collectState()
        setClickListeners()
    }

    private fun collectState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            //loading bar
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(CommentEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            commentAdapter?.apply {
                submitList(state.comments)
            }
        }
    }

    private fun initAdapter() {
        binding.rvComments.apply {
            layoutManager = LinearLayoutManager(
                this@CommentFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            commentAdapter = CommentAdapter(this@CommentFragment)
            adapter = commentAdapter
        }
    }

    override fun onCommentLike(commentId: String) {
        viewModel.onTriggerEvent(CommentEvent.LikeComment(commentId))
    }

    private fun setClickListeners() {
        binding.icLikePost.setOnClickListener {
            viewModel.onTriggerEvent(CommentEvent.LikeCurrentPost)
        }

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}