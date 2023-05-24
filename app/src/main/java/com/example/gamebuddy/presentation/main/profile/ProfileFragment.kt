package com.example.gamebuddy.presentation.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentProfileBinding
import com.example.gamebuddy.domain.model.profile.profilUser
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.main.match.MatchedGamesAdapter
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.EnvironmentManager
import com.example.gamebuddy.util.EnvironmentModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class ProfileFragment : BaseAuthFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var menu: Menu
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
        binding.toolbarProfile.setOnMenuItemClickListener { item ->
            if(item.itemId == R.id.action_settings)
            {

                val avatar = user[0].avatar
                findNavController().
                navigate(ProfileFragmentDirections.
                actionProfileFragmentToEditProfileFragment(user[0].username,user[0].age,avatar!!,user[0].games.toTypedArray(),user[0].keywords.toTypedArray()))
                true
            }else{
                false
            }
        }
        binding.friendsCount.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAllFriendsFragment())
        }
        collectState()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        inflater.inflate(R.menu.profile_menu, this.menu)
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
        val flexboxLayoutManagerGames = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        val flexboxLayoutManagerKeyword = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        binding.gamesProfile.apply {
            layoutManager = flexboxLayoutManagerGames
            adapter = MatchedGamesAdapter().apply { submitList(user[0].games) }
        }
        binding.rv2Character.apply {
            layoutManager = flexboxLayoutManagerKeyword
            adapter = MatchedGamesAdapter().apply { submitList(user[0].keywords) }
        }
        Glide.with(requireContext())
            .load(user[0].avatar)
            .into(binding.profileAvatar)
        if (user[0].joinedCommunities.isNullOrEmpty()){
            binding.communiesText.text = "You Did Not Join Any Community"
        }else{
            binding.communiesText.text = user[0].joinedCommunities.toString()
        }
        binding.profileUsername.text = user[0].username
        binding.friendsCount.text = user[0].friendsCount.toString()
        binding.ageText.text = user[0].age
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}