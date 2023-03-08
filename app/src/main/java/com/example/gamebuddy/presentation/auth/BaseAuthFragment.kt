package com.example.gamebuddy.presentation.auth

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.gamebuddy.presentation.UICommunicationListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
abstract class BaseAuthFragment: Fragment() {

    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            Timber.e("Must implement UICommunicationListener")
        }
    }

}