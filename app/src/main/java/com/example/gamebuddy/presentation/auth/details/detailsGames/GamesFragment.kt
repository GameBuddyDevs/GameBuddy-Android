package com.example.gamebuddy.presentation.auth.details.detailsGames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gamebuddy.databinding.FragmentGamesBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.auth.details.detailsUser.DetailsViewModel

class GamesFragment : BaseAuthFragment() {
    private val sharedViewModel: DetailsViewModel by activityViewModels()

    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Access the views using the binding object here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}