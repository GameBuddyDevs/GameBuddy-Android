package com.example.gamebuddy.presentation.main.match


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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

    private lateinit var flingContainer: SwipeFlingAdapterView
    private var al: ArrayList<User> = ArrayList()
    private lateinit var arrayAdapter: ArrayAdapter<User>
    private val viewModel: MatchViewModel by activityViewModels()

    private var _binding:FragmentMatchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMatchBinding.inflate(inflater,container,false)
        flingContainer = binding.frame

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onTriggerEvent(MatchEvent.GetUsers)
        al.add(User("test","test",20,"Turkey"))
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
                for(user in users){
                    al.add(user)
                }
            }
        }

    }
    private fun initAdapter(){
        arrayAdapter = MatchAdapter(requireContext(),al)
        flingContainer.adapter = arrayAdapter
        flingContainer.setFlingListener(object : SwipeFlingAdapterView.onFlingListener{
            override fun removeFirstObjectInAdapter() {
                al.removeAt(0)
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onLeftCardExit(dataObject: Any?) {
                Timber.d("Left")
            }

            override fun onRightCardExit(dataObject: Any?) {
                Toast.makeText(context,"MATCHING",Toast.LENGTH_LONG).show()
                findNavController().navigate(MatchFragmentDirections.actionMatchFragmentToSendMessageFragment())
                Timber.d("Right")
            }

            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {
                arrayAdapter.notifyDataSetChanged()
                Timber.d("LIST", "notified")
            }

            override fun onScroll(scrollProgressPercent: Float) {
                val view = flingContainer.selectedView
                view.findViewById<View>(R.id.item_swipe_right_indicator).alpha =
                    if (scrollProgressPercent < 0) -scrollProgressPercent else 0f
                view.findViewById<View>(R.id.item_swipe_left_indicator).alpha =
                    if (scrollProgressPercent > 0) scrollProgressPercent else 0f
            }
        })

        flingContainer.setOnItemClickListener { itemPosition, dataObject ->
            Timber.d("Clicked")
        }

        val right =binding.root.findViewById<View>(R.id.right)
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