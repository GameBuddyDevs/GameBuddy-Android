package com.example.gamebuddy.presentation.main.populargames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentCommunityDetailBinding
import com.example.gamebuddy.databinding.FragmentPopularGamesBinding
import com.example.gamebuddy.domain.model.popular.PopularGames
import com.example.gamebuddy.presentation.main.community.CommunityEvent
import com.example.gamebuddy.presentation.main.communitydetail.CommunityDetailAdapter
import com.example.gamebuddy.presentation.main.communitydetail.CommunityDetailViewModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue


class PopularGamesFragment : Fragment(), PopularGamesAdapter.OnClickListener {

    private val viewModel: PopularGamesViewModel by viewModels()

    private var _binding: FragmentPopularGamesBinding? = null
    private val binding get() = _binding!!

    private var popularGamesAdapter: PopularGamesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPopularGamesBinding.inflate(inflater, container, false)

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
                        viewModel.onTriggerEvent(PopularGamesEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            popularGamesAdapter?.apply {
                submitList(state.games)
            }

        }
    }

    override fun onGameClick(gameId: String) {
        val bundle = bundleOf("gameId" to gameId)
        findNavController().navigate(R.id.action_popularGamesFragment_to_gameDetailFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}