package com.example.gamebuddy.presentation.main.joincommunity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gamebuddy.R
import com.example.gamebuddy.data.remote.model.joincommunity.Community
import com.example.gamebuddy.databinding.FragmentJoinCommunityBinding
import com.example.gamebuddy.presentation.main.community.CommunityEvent
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
                        viewModel.onTriggerEvent(JoinCommunityEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            joinCommunityAdapter?.apply {
                submitList(state.communities)
            }
        }
    }

    override fun onCommunityClick(community: Community) {
        //bundlea gerek yok. Önce community çekecen eğer takip etmiyorsa zaten takip et butonu olacak fotolar da o sebepten ötürü gelmeyecek
        val bundle = bundleOf(
            "communityId" to community.communityId,
        )

        val action =
            JoinCommunityFragmentDirections.actionJoinCommunityFragmentToCommunityDetailFragment(
                community.name,
                community.wallpaper,
                community.communityAvatar,
                community.description,
                community.memberCount.toString(),
                "40",
                community.communityId,
            )

        findNavController().navigate(action)
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