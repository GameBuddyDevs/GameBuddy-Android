package com.example.gamebuddy.presentation.main.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.databinding.FragmentAchievementBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.main.chatbox.FriendsAdapter
import com.example.gamebuddy.util.*
import timber.log.Timber


class AchievementFragment : BaseAuthFragment(), AchievementAdapter.OnClickListener {

    private val viewModel: AchievementViewModel by activityViewModels()

    private var _binding: FragmentAchievementBinding? = null
    private val binding get() = _binding!!

    private var achievementAdapter: AchievementAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAchievementBinding.inflate(inflater, container, false)

        updateEnvironment(apiType = ApiType.APPLICATION, deploymentType = DeploymentType.PRODUCTION)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        collectState()
        initRecyclerView()
    }

    private fun collectState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(AchievementEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            achievementAdapter?.apply {
                submitList(state.achievements)
            }
        }
    }


    private fun initRecyclerView() {
        binding.recyclerViewAchievements.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            achievementAdapter = AchievementAdapter(this@AchievementFragment)
            adapter = achievementAdapter
        }
    }

    override fun onItemClick(achievementId: String) {
        viewModel.onTriggerEvent(AchievementEvent.CollectAchievement(achievementId))
        Toast.makeText(requireContext(), "Achievement Collected", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun updateEnvironment(
        apiType: ApiType,
        deploymentType: DeploymentType
    ) {
        val index = EnvironmentManager.environments.indexOfFirst { it.apiType == apiType }
        EnvironmentManager.environments[index] = EnvironmentModel(
            apiType = apiType,
            deploymentType = deploymentType,
            path = "application/"
        )
    }

}