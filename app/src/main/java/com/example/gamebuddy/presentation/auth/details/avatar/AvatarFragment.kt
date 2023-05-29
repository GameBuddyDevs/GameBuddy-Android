package com.example.gamebuddy.presentation.auth.details.avatar

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gamebuddy.databinding.FragmentAvatarBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.auth.details.DetailsEvent
import com.example.gamebuddy.presentation.auth.details.DetailsViewModel
import com.example.gamebuddy.util.*
import timber.log.Timber

class AvatarFragment : BaseAuthFragment(), AvatarAdapter.OnClickListener {
    private val detailsViewModel: DetailsViewModel by activityViewModels()
    private var avatarAdapter: AvatarAdapter? = null
    private var _binding: FragmentAvatarBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvatarBinding.inflate(inflater, container, false)

        updateEnvironment(apiType = ApiType.APPLICATION, deploymentType = DeploymentType.PRODUCTION)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsViewModel.onTriggerEvent(DetailsEvent.GetAvatars)
       // setClickListeners()
        initRecyclerView()
        collectState()
    }

    private fun initRecyclerView() {
        binding.rvAvatarList.apply {
            layoutManager = GridLayoutManager(this@AvatarFragment.context,2)
            avatarAdapter = AvatarAdapter(this@AvatarFragment)
            adapter = avatarAdapter
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

            avatarAdapter?.apply {
                submitList(state.avatars)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        avatarAdapter = null
        _binding = null
    }

    override fun onItemClick(position: Int, avatarId: String) {
        Timber.d("onItemClick Message $position : $avatarId")
        detailsViewModel.onTriggerEvent(DetailsEvent.AddAvatarToSelected(avatarId))
        val action = AvatarFragmentDirections.actionAvatarFragmentToGamesFragment()
        findNavController().navigate(action)
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
