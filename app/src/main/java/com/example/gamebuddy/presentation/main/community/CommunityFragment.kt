package com.example.gamebuddy.presentation.main.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentCommunityBinding
import com.example.gamebuddy.presentation.dialog.CommunityDialogFragment
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
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
            layoutManager = LinearLayoutManager(context)
            postAdapter = PostAdapter(this@CommunityFragment)
            adapter = postAdapter
        }
    }

    private fun setClickListeners() {
        val bottomSheet = CommunityDialogFragment()
        bottomSheet.setOnActionCompleteListener(object : CommunityDialogFragment.OnActionSelectedListener {
            override fun onActionSelected(destination: Int) {
                bottomSheet.dismiss()
                findNavController().navigate(destination)
            }
        })

        binding.apply {
            icCommunitiesMenu.setOnClickListener {
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }
    }

    override fun onLikePostClick(postId: String) {
        viewModel.onTriggerEvent(CommunityEvent.LikePost(postId))
    }

    override fun onCommentClick(postId: String) {
        val bundle = bundleOf("postId" to postId)
        findNavController().navigate(R.id.action_communityFragment_to_commentFragment, bundle)
    }

}