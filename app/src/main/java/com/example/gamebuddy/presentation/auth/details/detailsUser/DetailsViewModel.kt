package com.example.gamebuddy.presentation.auth.details.detailsUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamebuddy.presentation.auth.details.detailsUser.DetailsUserEvent

class DetailsViewModel : ViewModel() {
    private val _detailsUserEvent = MutableLiveData<DetailsUserEvent>()
    val detailsUserEvent: LiveData<DetailsUserEvent> = _detailsUserEvent
    private val _games = MutableLiveData<List<String>>()
    val games: LiveData<List<String>> = _games

    fun onDetailsUserEvent(event: DetailsUserEvent) {
        _detailsUserEvent.value = event
    }

    fun onTriggerEvent(event: DetailsUserEvent) {

    }
}