package com.example.gamebuddy.presentation.main.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.gamebuddy.databinding.FragmentAchievementBinding
import com.example.gamebuddy.databinding.FragmentMarketBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.main.market.MarketEvent
import com.example.gamebuddy.util.*
import timber.log.Timber


class AchievementFragment : BaseAuthFragment() {
    private val viewModel: AchievementViewModel by activityViewModels()
    private var _binding: FragmentAchievementBinding? = null
    private val binding get() = _binding!!
    private lateinit var earnedAdapter: AchievementAdapter
    private lateinit var collectedAdapter: AchievementAdapter
    private lateinit var unearnedAdapter: AchievementAdapter

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
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        viewModel.onTriggerEvent(AchievementEvent.GetAchievement)
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
            earnedAdapter.submitList(state.earnedAchievements)
            collectedAdapter.submitList(state.collectedAchievements)
            unearnedAdapter.submitList(state.unearnedAchievements)
        }
    }


    private fun initRecyclerView() {
        earnedAdapter = AchievementAdapter(object : AchievementAdapter.OnClickListener {
            override fun onItemClick(position: Int, achievementId: String) {
                Timber.d("onItemClick Message $position : $achievementId")
            }
        })
        binding.recyclerViewAchievements.adapter = earnedAdapter

        collectedAdapter = AchievementAdapter(object : AchievementAdapter.OnClickListener {
            override fun onItemClick(position: Int, achievementId: String) {
                Timber.d("onItemClick Message $position : $achievementId")
            }
        })
        binding.recyclerViewCollected.adapter = collectedAdapter

        unearnedAdapter = AchievementAdapter(object : AchievementAdapter.OnClickListener {
            override fun onItemClick(position: Int, achievementId: String) {
                Timber.d("onItemClick Message $position : $achievementId")
            }
        })
        binding.recyclerViewUnearned.adapter = unearnedAdapter
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun onItemClick(position: Int, achievementId: String) {
        Timber.d("onItemClick Message $position : $achievementId")
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