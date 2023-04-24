package com.example.gamebuddy.presentation.auth.details.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.databinding.FragmentGamesBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.auth.details.DetailsEvent
import com.example.gamebuddy.presentation.auth.details.DetailsViewModel
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.EnvironmentManager
import com.example.gamebuddy.util.EnvironmentModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import timber.log.Timber

class GamesFragment : BaseAuthFragment(), GamesAdapter.OnClickListener {

    private val detailsViewModel: DetailsViewModel by activityViewModels()

    private var gamesAdapter: GamesAdapter? = null

    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)

        updateEnvironment(
            apiType = ApiType.APPLICATION,
            deploymentType = DeploymentType.PRODUCTION
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsViewModel.onTriggerEvent(DetailsEvent.GetGames)

        setClickListeners()
        initRecyclerView()
        collectState()
    }

    private fun setClickListeners() {
        binding.btnDetailsUser.setOnClickListener {
            findNavController().navigate(GamesFragmentDirections.actionGamesFragmentToKeywordFragment())
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewGames.apply {
            layoutManager = LinearLayoutManager(context)
            gamesAdapter = GamesAdapter(this@GamesFragment).also {
                adapter = it
            }
        }
    }

    private fun collectState() {
        detailsViewModel.detailsUiState.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        detailsViewModel.onTriggerEvent(DetailsEvent.OnRemoveHeadFromQueue)
                    }
                }
            )


            gamesAdapter?.apply {
                submitList(state.games)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        gamesAdapter = null
        _binding = null
    }

    override fun onItemClick(position: Int, gameId: String) {
        Timber.d("onItemClick: $position")
        detailsViewModel.onTriggerEvent(DetailsEvent.AddGameToSelected(gameId))
    }

    private fun updateEnvironment(
        apiType: ApiType,
        deploymentType: DeploymentType
    ) {
        val index =
            EnvironmentManager.environments.indexOfFirst { it.apiType == apiType }
        EnvironmentManager.environments[index] = EnvironmentModel(
            apiType = apiType,
            deploymentType = deploymentType,
            path = "application/"
        )
    }
}