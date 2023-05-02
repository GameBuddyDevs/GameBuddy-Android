package com.example.gamebuddy.presentation.main.match


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentMatchBinding
import com.example.gamebuddy.domain.model.user.User
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import timber.log.Timber

class MatchFragment : BaseAuthFragment() {

    private var matchedUsers: ArrayList<User> = ArrayList()
    private lateinit var flingContainer: SwipeFlingAdapterView
    private lateinit var arrayAdapter: ArrayAdapter<User>

    private val viewModel: MatchViewModel by activityViewModels()

    private var _binding: FragmentMatchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMatchBinding.inflate(inflater, container, false)
        flingContainer = binding.frame

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onTriggerEvent(MatchEvent.GetUsers)
        matchedUsers.add(
            User(
                "1",
                "Romero",
                20,
                "Italy",
                games = listOf("GTA", "CSGO", "PUBG"),
                keywords = listOf("aim master", "clutch king", "pro")
            )
        )
        initAdapter()
        collectState()

    }

    private fun collectState() {
        viewModel.usersUiState.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(MatchEvent.OnRemoveHeadFromQueue)
                    }
                }
            )
            state.users?.let { users ->
                for (user in users) {
                    matchedUsers.add(user)
                }
            }
        }

    }

    private fun initAdapter() {
        arrayAdapter = MatchAdapter(requireContext(), matchedUsers)
        flingContainer.adapter = arrayAdapter
        flingContainer.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                matchedUsers.removeAt(0)
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onLeftCardExit(dataObject: Any?) {
                Timber.d("Left")
            }

            override fun onRightCardExit(dataObject: Any?) {
                Timber.d("Right")
                val user = dataObject as User
                val action = MatchFragmentDirections.actionMatchFragmentToChatFragment(user.userId, user.gamerUsername!!, user.avatar!!)
                findNavController().navigate(action)
            }

            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {
                Timber.d("LIST", "notified")
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onScroll(scrollProgressPercent: Float) {
                val view = flingContainer.selectedView
                view.findViewById<View>(R.id.item_swipe_right_indicator).alpha =
                    if (scrollProgressPercent < 0) -scrollProgressPercent else 0f
                view.findViewById<View>(R.id.item_swipe_left_indicator).alpha =
                    if (scrollProgressPercent > 0) scrollProgressPercent else 0f
            }
        })

        flingContainer.setOnItemClickListener { _, _ ->
            Timber.d("Clicked")
        }

        val right = binding.root.findViewById<View>(R.id.right)
        right.setOnClickListener {
            flingContainer.topCardListener.selectRight()
        }

        val left = binding.root.findViewById<View>(R.id.left)
        left.setOnClickListener {
            flingContainer.topCardListener.selectLeft()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}