package com.example.gamebuddy.presentation.auth.details.keywords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.gamebuddy.databinding.FragmentKeywordBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.auth.details.DetailsEvent
import com.example.gamebuddy.presentation.auth.details.DetailsViewModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue

class KeywordFragment : BaseAuthFragment() {

    private val detailsViewModel: DetailsViewModel by activityViewModels()

    private var keywordAdapter: KeywordAdapter? = null

    private var _binding: FragmentKeywordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeywordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        collectState()
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


            keywordAdapter?.apply {
                submitList(state.games)
            }
        }
    }

    private fun initRecyclerView() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}