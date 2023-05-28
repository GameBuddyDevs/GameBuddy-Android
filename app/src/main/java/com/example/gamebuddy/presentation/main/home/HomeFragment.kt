package com.example.gamebuddy.presentation.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.databinding.FragmentHomeBinding
import com.example.gamebuddy.domain.model.Pending.PendingFriends
import com.example.gamebuddy.domain.model.popular.PopularGames
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import timber.log.Timber


class HomeFragment : BaseAuthFragment(), PendingFriendAdapter.OnClickListener, GetPopularAdapter.OnClickListener {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var pendingFriendAdapter: PendingFriendAdapter? = null
    private var getPopularAdapter: GetPopularAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.btnStartMatching.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMatchFragment())
        }
        updateEnvironment(apiType = ApiType.APPLICATION, deploymentType = DeploymentType.PRODUCTION)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setClickListeners()
        collectState()
    }

    private fun setClickListeners() {
        binding.apply {
            txtSeeAllPopular.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPopularGamesFragment())
            }
        }
    }

    private fun collectState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(HomeEvent.OnRemoveHeadFromQueue)
                    }
                }
            )
            pendingFriendAdapter?.apply {
                submitList(state.pendingFriends)
            }
            getPopularAdapter?.apply {
                submitList(state.popularGames)
            }

        }
    }
    private fun stateFriends(userId:String,accept:Boolean){
        viewModel.onTriggerEvent(HomeEvent.OnSetUserId(userId))
        viewModel.onTriggerEvent(HomeEvent.OnSetAcceptFriend(accept))
        viewModel.onTriggerEvent(HomeEvent.AcceptFriends)
        viewModel.onTriggerEvent(HomeEvent.ResetPendingFriends)
        pendingFriendAdapter?.apply {
            submitList(viewModel.uiState.value?.pendingFriends)
        }
    }
    private fun initRecyclerView(){
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.context, LinearLayoutManager.HORIZONTAL, false)
            pendingFriendAdapter = PendingFriendAdapter(this@HomeFragment)
            adapter = pendingFriendAdapter
        }
        binding.rvPopular.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.context, LinearLayoutManager.HORIZONTAL, false)
            getPopularAdapter = GetPopularAdapter(this@HomeFragment)
            adapter = getPopularAdapter
        }
    }
    override fun onItemClick(position: Int, item: PendingFriends, accept:Boolean) {
        Timber.d("onItemClick Message $position : $item : $accept")
        if(accept) Toast.makeText(context,"Accepted Friend Username: ${item.username}",Toast.LENGTH_LONG).show() else Toast.makeText(context,"Rejected Friend Username: ${item.username}",Toast.LENGTH_LONG).show()
        stateFriends(item.userId,accept)
    }
    override fun onItemClick(position: Int, item: PopularGames) {
        Timber.d("onItemClick Message $position : $item")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}