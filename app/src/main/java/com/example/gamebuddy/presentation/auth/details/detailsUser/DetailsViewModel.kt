package com.example.gamebuddy.presentation.auth.details.detailsUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamebuddy.domain.usecase.auth.GamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val gamesUseCase: GamesUseCase,
): ViewModel() {
    private val _detailsUserEvent = MutableLiveData<DetailsUserEvent>()
    val detailsUserEvent: LiveData<DetailsUserEvent> = _detailsUserEvent

    private val _games = MutableLiveData<List<String>>()
    val games: LiveData<List<String>> = _games

    private val _keywords = MutableLiveData<List<String>>()
    val keywords: LiveData<List<String>> =_keywords

    private fun onDetailsUserEvent(event: DetailsUserEvent) {
        _detailsUserEvent.value = event
    }
    suspend fun getGame(){
        val games = gamesUseCase.execute()

    }
    fun onTriggerEvent(event: DetailsUserEvent) {
        onDetailsUserEvent(event)
    }
}