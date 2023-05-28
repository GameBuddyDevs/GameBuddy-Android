package com.example.gamebuddy.presentation.dialog

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.FragmentCommunityDialogListDialogBinding


class CommunityDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCommunityDialogListDialogBinding? = null

    private lateinit var listener: OnActionSelectedListener

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.apply {
            cvExploreCommunities.setOnClickListener { listener.onActionSelected(R.id.action_communityFragment_to_joinCommunityFragment) }
            //cvCreateACommunity.setOnClickListener { listener.onActionSelected(R.id.action_communityFragment_to_createCommunityFragment) }
            //cvAddAPhoto.setOnClickListener { listener.onActionSelected(R.id.action_communityFragment_to_addPhotoFragment) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setOnActionCompleteListener(listener: OnActionSelectedListener) {
        this.listener = listener
    }

    interface OnActionSelectedListener {
        fun onActionSelected(destination: Int)
    }
}