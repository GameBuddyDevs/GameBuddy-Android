package com.example.gamebuddy.presentation.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentEditProfileBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.main.match.MatchedGamesAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class EditProfileFragment : BaseAuthFragment() {
    private val viewModel: EditProfileViewModel by activityViewModels()

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var menu: Menu
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater,container,false)

        val keywords = EditProfileFragmentArgs.fromBundle(requireArguments()).keywords.toList()
        val games = EditProfileFragmentArgs.fromBundle(requireArguments()).games.toList()
        val age = EditProfileFragmentArgs.fromBundle(requireArguments()).age
        val username = EditProfileFragmentArgs.fromBundle(requireArguments()).username
        val avatar = EditProfileFragmentArgs.fromBundle(requireArguments()).avatar

        binding.usernameEditText.hint = username
        binding.ageEditText.hint = age
        if (binding.profileAvatar != null) {
            Glide.with(requireContext())
                .load(avatar)
                .into(binding.profileAvatar)
        }
        initAdapter(games,keywords)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarEditprofile.setOnMenuItemClickListener { item->
            if(item.itemId == R.id.action_save){
                if(binding.usernameEditText.text.isEmpty() && binding.ageEditText.text.isEmpty()){
                    Toast.makeText(context, "Edit username or/and age for saving",Toast.LENGTH_LONG).show()
                }
                if(binding.usernameEditText.text.isNotEmpty()){
                    viewModel.onTriggerEvent(EditEvent.OnSetParam("username"))
                    viewModel.onTriggerEvent(EditEvent.OnSetUsername(binding.usernameEditText.text.toString()))
                    viewModel.onTriggerEvent(EditEvent.Edit)
                    findNavController().popBackStack(R.id.profileFragment,true)
                }
                if(binding.ageEditText.text.isNotEmpty()){
                    viewModel.onTriggerEvent(EditEvent.OnSetParam("age"))
                    viewModel.onTriggerEvent(EditEvent.OnSetAge(binding.ageEditText.text.toString()))
                    viewModel.onTriggerEvent(EditEvent.Edit)
                    findNavController().popBackStack(R.id.profileFragment,true)
                }
                true
            }else if (item.itemId == R.id.action_back){
                findNavController().popBackStack(R.id.profileFragment,true)
                true
            }else{
                false
            }
        }
        binding.gamesEditButton.setOnClickListener {
            findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToEditGameFragment())
        }
        binding.keywordEditButton.setOnClickListener {
            findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToEditKeywordFragment())
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        inflater.inflate(R.menu.profile_menu, this.menu)
    }
    private fun initAdapter(games:List<String>, keywords:List<String>){
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
            adapter = MatchedGamesAdapter().apply { submitList(games) }
        }
        binding.rv2Character.apply {
            layoutManager = flexboxLayoutManagerKeyword
            adapter = MatchedGamesAdapter().apply { submitList(keywords) }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}