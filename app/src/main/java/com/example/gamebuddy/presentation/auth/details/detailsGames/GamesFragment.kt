package com.example.gamebuddy.presentation.auth.details.detailsGames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.gamebuddy.databinding.FragmentGamesBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.auth.details.detailsUser.DetailsViewModel
import kotlinx.coroutines.launch

class GamesFragment : BaseAuthFragment() {
    private val sharedViewModel: DetailsViewModel by activityViewModels()
    private var gamesAdapter: GamesAdapter? = null


    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)

        getGames()

        return binding.root
    }

    private fun getGames(){
        lifecycleScope.launch{
            sharedViewModel.getGame()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}