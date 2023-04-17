package com.example.gamebuddy.presentation.auth.details.keywords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.databinding.FragmentKeywordBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.auth.details.DetailsEvent
import com.example.gamebuddy.presentation.auth.details.DetailsViewModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue

class KeywordFragment : BaseAuthFragment(), KeywordAdapter.OnClickListener {

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

        detailsViewModel.onTriggerEvent(DetailsEvent.GetKeywords)
        setClickListeners()
        initRecyclerView()
        collectState()
    }

    private fun setClickListeners() {
        binding.btnFinishDetail.setOnClickListener {
            detailsViewModel.onTriggerEvent(DetailsEvent.SendProfileDetail)
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


            keywordAdapter?.apply {
                submitList(state.keywords)
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewGames.apply {
            layoutManager = LinearLayoutManager(context)
            keywordAdapter = KeywordAdapter(this@KeywordFragment).also {
                adapter = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int, keywordId: String) {
        detailsViewModel.onTriggerEvent(DetailsEvent.AddKeywordToSelected(keywordId))
    }
}