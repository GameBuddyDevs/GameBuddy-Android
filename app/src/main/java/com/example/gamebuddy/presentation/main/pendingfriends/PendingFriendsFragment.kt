package com.example.gamebuddy.presentation.main.pendingfriends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.databinding.FragmentPendingFriendsBinding
import com.example.gamebuddy.domain.model.Pending.PendingFriends
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.main.home.HomeEvent
import com.example.gamebuddy.presentation.main.home.HomeViewModel
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import timber.log.Timber

class PendingFriendsFragment : BaseAuthFragment(), PendingAllFriendsAdapter.OnClickListener {
    private val viewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentPendingFriendsBinding? = null
    private val binding get() = _binding!!
    private var pendingAllFriendAdapter: PendingAllFriendsAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPendingFriendsBinding.inflate(inflater,container,false)
        updateEnvironment(apiType = ApiType.APPLICATION, deploymentType = DeploymentType.PRODUCTION)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onTriggerEvent(HomeEvent.GetPendingFriends)
        initRecyclerView()
        collectState()

        binding.apply {
            icBack.setOnClickListener {
                findNavController().popBackStack()
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
            pendingAllFriendAdapter?.apply {
                submitList(state.pendingFriends)
            }
        }
    }
    private fun initRecyclerView(){
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(this@PendingFriendsFragment.context, LinearLayoutManager.VERTICAL, false)
            pendingAllFriendAdapter = PendingAllFriendsAdapter(this@PendingFriendsFragment)
            adapter = pendingAllFriendAdapter
        }
    }
    private fun stateFriends(userId:String,accept:Boolean){
        viewModel.onTriggerEvent(HomeEvent.OnSetUserId(userId))
        viewModel.onTriggerEvent(HomeEvent.OnSetAcceptFriend(accept))
        viewModel.onTriggerEvent(HomeEvent.AcceptFriends)
        viewModel.onTriggerEvent(HomeEvent.ResetPendingFriends)
        pendingAllFriendAdapter?.apply {
            submitList(viewModel.uiState.value?.pendingFriends)
        }
    }
    override fun onItemClick(position: Int, item: PendingFriends, accept:Boolean) {
        Timber.d("onItemClick Message $position : $item : $accept")
        if(accept) Toast.makeText(context,"Accepted Friend Username: ${item.username}", Toast.LENGTH_LONG).show() else Toast.makeText(context,"Rejected Friend Username: ${item.username}",
            Toast.LENGTH_LONG).show()
        stateFriends(item.userId,accept)
    }
}