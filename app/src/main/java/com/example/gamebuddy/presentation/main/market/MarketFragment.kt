package com.example.gamebuddy.presentation.main.market

import android.app.SearchManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamebuddy.R
import android.content.Context
import android.view.Menu
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamebuddy.databinding.FragmentMarketBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.main.market.MarketAdapter
import com.example.gamebuddy.presentation.main.market.MarketEvent
import com.example.gamebuddy.presentation.main.market.MarketViewModel
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import timber.log.Timber

class MarketFragment : BaseAuthFragment() {
    private val viewModel: MarketViewModel by activityViewModels()
    private lateinit var searchView: SearchView
    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!
    private var marketAdapter: MarketAdapter? = null
    private lateinit var menu: Menu


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        viewModel.onTriggerEvent(MarketEvent.GetAvatars)
        initRecyclerView()
        collectState()
        initSearchView()
    }

    private fun collectState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(MarketEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            marketAdapter?.apply {
                submitList(state.avatars)
            }
        }
    }

    private fun initSearchView() {
        activity?.apply {
            Timber.d("initSearchView: called")
            val searchManager: SearchManager =
                getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.isSubmitButtonEnabled = true
        }

        // ENTER ON COMPUTER KEYBOARD OR ARROW ON VIRTUAL KEYBOARD
        val searchPlate =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText

        // set initial value of query text after rotation/navigation
        viewModel.uiState.value?.let { state ->
            if (state.query.isNotBlank()) {
                searchPlate.setText(state.query)
                searchView.isIconified = false
                binding.focusableView.requestFocus()
            }
        }
        searchPlate.setOnEditorActionListener { v, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = v.text.toString()
                Timber.d("SearchView: (keyboard) executing search...: $searchQuery")
                executeNewQuery(searchQuery)
            }
            true
        }

        // SEARCH BUTTON CLICKED (in toolbar)
        val searchButton = searchView.findViewById(androidx.appcompat.R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            Timber.d("SearchView: (search button) executing search...: $searchQuery")
            executeNewQuery(searchQuery)
        }
    }

    private fun executeNewQuery(query: String) {
        viewModel.onTriggerEvent(MarketEvent.UpdateQuery(query))
        viewModel.onTriggerEvent(MarketEvent.NewQuery)
        resetUI()
    }

    private fun resetUI() {
        uiCommunicationListener.hideSoftKeyboard()
        binding.focusableView.requestFocus()
    }

    private fun initRecyclerView() {
        binding.rvAvatarList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = marketAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}