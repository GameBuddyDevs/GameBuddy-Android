package com.example.gamebuddy.presentation.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.databinding.FragmentAllFriendsBinding
import com.example.gamebuddy.domain.model.popular.PopularGames
import com.example.gamebuddy.domain.model.profile.AllFriends
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import timber.log.Timber

class AllFriendsFragment : BaseAuthFragment(), AllFriendsAdapter.OnClickListener {
    private var allFriendsAdapter: AllFriendsAdapter? = null

    private val viewModel: AllFriendsViewModel by viewModels()
    private var _binding: FragmentAllFriendsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllFriendsBinding.inflate(inflater, container, false)
        updateEnvironment(apiType = ApiType.APPLICATION, deploymentType = DeploymentType.PRODUCTION)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        viewModel.onTriggerEvent(AllFriendsEvent.GetAllFriends)
        collectState()
    }
    private fun collectState() {
        viewModel.usersUiState.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(AllFriendsEvent.OnRemoveHeadFromQueue)
                    }
                }
            )
            allFriendsAdapter?.apply {
                submitList(state.allFriends)
            }
        }
    }
    private fun removeFriend(userId:String){
        viewModel.onTriggerEvent(AllFriendsEvent.OnSetUserId(userId))
        viewModel.onTriggerEvent(AllFriendsEvent.RemoveFriend)
        viewModel.onTriggerEvent(AllFriendsEvent.ResetAllFriends)
        allFriendsAdapter?.apply {
            submitList(viewModel.usersUiState.value?.allFriends)
        }
    }
    private fun initRecyclerView(){
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(this@AllFriendsFragment.context,LinearLayoutManager.VERTICAL,false)
            allFriendsAdapter = AllFriendsAdapter(this@AllFriendsFragment)
            adapter = allFriendsAdapter
        }
    }
    override fun onItemClick(position: Int, item: AllFriends) {
        Timber.d("onItemClick Message $position : $item")
        Toast.makeText(context,"Removed Successfully ${item.username}",Toast.LENGTH_LONG).show()
        removeFriend(item.userId)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}