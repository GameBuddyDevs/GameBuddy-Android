package com.example.gamebuddy.presentation.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.example.gamebuddy.databinding.FragmentProfileBinding
import com.example.gamebuddy.domain.model.profile.profilUser
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.EnvironmentManager
import com.example.gamebuddy.util.EnvironmentModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue

class ProfileFragment : BaseAuthFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var arrayAdapter: ArrayAdapter<profilUser>

    private val viewModel: ProfileViewModel by activityViewModels()

    private var user: ArrayList<profilUser> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val index = EnvironmentManager.environments.indexOfFirst { it.apiType == ApiType.APPLICATION }
        EnvironmentManager.environments[index] = EnvironmentModel(
            apiType = ApiType.APPLICATION,
            deploymentType = DeploymentType.PRODUCTION,
            path = "application/"
        )
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onTriggerEvent(ProfileEvent.GetUserInfo)
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
                        viewModel.onTriggerEvent(ProfileEvent.OnRemoveHeadFromQueue)
                    }
                }
            )
            state.profileUser?.let { profileUsers ->
                user.add(profileUsers)
                initAdapter()
            }
        }
    }
    private fun initAdapter(){
        arrayAdapter = ProfileAdapter(requireContext(), user)
        binding.simpleListView.adapter = arrayAdapter
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}