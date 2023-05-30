package com.example.gamebuddy.presentation.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentEditAvatarBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.presentation.auth.details.avatar.AvatarAdapter
import com.example.gamebuddy.util.StateMessageCallback
import com.example.gamebuddy.util.processQueue

class EditAvatarFragment : BaseAuthFragment(), AvatarAdapter.OnClickListener {
    private var _binding: FragmentEditAvatarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditProfileViewModel by activityViewModels()
    private var avatarAdapter: AvatarAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditAvatarBinding.inflate(inflater,container,false)


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onTriggerEvent(EditEvent.GetAvatars)
        initRecyclerView()
        collectState()
    }
    private fun selectAvatar(avatarId: String){
        viewModel.onTriggerEvent(EditEvent.OnSetAvatar(avatarId = avatarId))
        viewModel.onTriggerEvent(EditEvent.OnSetParam(param = "avatar"))
        viewModel.onTriggerEvent(EditEvent.Edit)
        findNavController().popBackStack(R.id.editProfileFragment,true)
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
            avatarAdapter?.apply {
                submitList(state.avatars)
            }
        }
    }
    private fun initRecyclerView() {
        binding.rvAvatarList.apply {
            layoutManager = GridLayoutManager(this@EditAvatarFragment.context,2)
            avatarAdapter = AvatarAdapter(this@EditAvatarFragment)
            adapter = avatarAdapter
        }
    }
    override fun onItemClick(position: Int, avatarId: String) {
        Toast.makeText(context,"Selected", Toast.LENGTH_LONG).show()
        selectAvatar(avatarId)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        avatarAdapter = null
        _binding = null
    }
}