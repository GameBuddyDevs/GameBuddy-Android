package com.example.gamebuddy.presentation.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentEditKeywordBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.auth.details.keywords.KeywordAdapter
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue

class EditKeywordFragment : BaseAuthFragment(), KeywordAdapter.OnClickListener {
    private var keywordAdapter: KeywordAdapter? = null
    private val viewModel: EditProfileViewModel by activityViewModels()
    private var _binding: FragmentEditKeywordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditKeywordBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onTriggerEvent(EditEvent.GetKeywords)
        setClickListeners()
        initRecyclerView()
        collectState()
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
            keywordAdapter?.apply {
                submitList(state.keywords)
            }
        }
    }
    private fun setClickListeners() {
        binding.btnFinishDetail.setOnClickListener {
            viewModel.onTriggerEvent(EditEvent.OnSetParam("keywords"))
            viewModel.onTriggerEvent(EditEvent.Edit)
            findNavController().navigate(EditKeywordFragmentDirections.actionEditKeywordFragmentToEditLoadingFragment())
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack(R.id.profileFragment,false)
        }
    }
    private fun initRecyclerView() {
        binding.recyclerViewGames.apply {
            layoutManager = LinearLayoutManager(context)
            keywordAdapter = KeywordAdapter(this@EditKeywordFragment).also {
                adapter = it
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        keywordAdapter = null
    }
    override fun onItemClick(position: Int, keywordId: String) {
        viewModel.onTriggerEvent(EditEvent.AddKeywordToSelected(keywordId))
    }
}