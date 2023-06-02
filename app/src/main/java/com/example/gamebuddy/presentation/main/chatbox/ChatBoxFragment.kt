package com.example.gamebuddy.presentation.main.chatbox

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.gamebuddy.R
import com.example.gamebuddy.data.remote.model.chatbox.Inbox
import com.example.gamebuddy.databinding.FragmentChatBoxBinding
import com.example.gamebuddy.domain.model.Friend
import com.example.gamebuddy.presentation.main.match.MatchFragmentDirections
import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import timber.log.Timber


class ChatBoxFragment : BaseChatFragment(), ChatBoxAdapter.OnClickListener, FriendsAdapter.OnFriendClickListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var searchView: SearchView
    private lateinit var menu: Menu

    private var chatBoxAdapter: ChatBoxAdapter? = null
    private var friendsAdapter: FriendsAdapter? = null

    private val viewModel: ChatBoxViewModel by viewModels()

    private var _binding: FragmentChatBoxBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBoxBinding.inflate(inflater, container, false)

        updateEnvironment(apiType = ApiType.APPLICATION, deploymentType = DeploymentType.PRODUCTION)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //(activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        // Set up the toolbar.
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        binding.swipeRefresh.setOnRefreshListener(this)
        initRecyclerView()
        collectState()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        inflater.inflate(R.menu.search_menu, this.menu)
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
                        viewModel.onTriggerEvent(ChatBoxEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            friendsAdapter?.apply {
                submitList(state.friends)
            }

            chatBoxAdapter?.apply {
                submitList(state.chatBox)
            }
        }
    }

    private fun initSearchView() {
        activity?.apply {
            Timber.d("initSearchView: called")
            val searchManager: SearchManager = getSystemService(SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.isSubmitButtonEnabled = true
        }

        // ENTER ON COMPUTER KEYBOARD OR ARROW ON VIRTUAL KEYBOARD
        val searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText

        // set initial value of query text after rotation/navigation
        viewModel.uiState.value?.let { state ->
            if(state.query.isNotBlank()){
                searchPlate.setText(state.query)
                searchView.isIconified = false
                binding.focusableView.requestFocus()
            }
        }
        searchPlate.setOnEditorActionListener { v, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH ) {
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

    private fun initRecyclerView() {
        binding.recyclerViewFriends.apply {
            layoutManager = LinearLayoutManager(this@ChatBoxFragment.context, LinearLayoutManager.HORIZONTAL, false)
            friendsAdapter = FriendsAdapter(this@ChatBoxFragment)
            adapter = friendsAdapter
        }

        binding.recyclerViewMessage.apply {
            layoutManager = LinearLayoutManager(this@ChatBoxFragment.context)
            chatBoxAdapter = ChatBoxAdapter(this@ChatBoxFragment)
            adapter = chatBoxAdapter
            //chatBoxAdapter!!.notifyDataSetChanged()
        }
    }

    private fun executeNewQuery(query: String){
        viewModel.onTriggerEvent(ChatBoxEvent.UpdateQuery(query))
        viewModel.onTriggerEvent(ChatBoxEvent.NewQuery)
        resetUI()
    }

    private  fun resetUI(){
        uiCommunicationListener.hideSoftKeyboard()
        binding.focusableView.requestFocus()
    }

    override fun onRefresh() {
        viewModel.onTriggerEvent(ChatBoxEvent.NewSearch)
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(position: Int, item: Friend) {
        Timber.d("onItemClick Friend $position : $item")
    }

    override fun onItemClick(item: Inbox) {
        val action = ChatBoxFragmentDirections.actionMessageFragmentToChatFragment2(item.userId, item.avatar, item.username)
        findNavController().navigate(action)
    }


}