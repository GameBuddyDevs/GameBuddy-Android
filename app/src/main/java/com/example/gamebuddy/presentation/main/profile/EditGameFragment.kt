package com.example.gamebuddy.presentation.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentEditGameBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.auth.details.games.GamesAdapter
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import timber.log.Timber

class EditGameFragment : BaseAuthFragment(), GamesAdapter.OnClickListener {
    private var gamesAdapter: GamesAdapter? = null
    private val viewModel: EditProfileViewModel by activityViewModels()
    private var _binding:FragmentEditGameBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditGameBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onTriggerEvent(EditEvent.GetGames)
        setClickListeners()
        initRecyclerView()
        collectState()
    }
    private fun setClickListeners() {
        binding.btnDetailsUser.setOnClickListener {
            viewModel.onTriggerEvent(EditEvent.OnSetParam("games"))
            viewModel.onTriggerEvent(EditEvent.Edit)
            findNavController().navigate(EditGameFragmentDirections.actionEditGameFragmentToEditLoadingFragment())
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack(R.id.editProfileFragment,false)
        }
    }
    private fun collectState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(EditEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            gamesAdapter?.apply {
                submitList(state.games)
            }
        }
    }
    private fun initRecyclerView() {
        binding.recyclerViewGames.apply {
            layoutManager = LinearLayoutManager(context)
            gamesAdapter = GamesAdapter(this@EditGameFragment).also {
                adapter = it
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
        viewModel.onTriggerEvent(EditEvent.AddGameToSelected(gameId))
    }
}