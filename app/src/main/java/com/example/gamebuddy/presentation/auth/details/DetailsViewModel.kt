package com.example.gamebuddy.presentation.auth.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.data.remote.request.setdetails.SetProfileDetailsRequest
import com.example.gamebuddy.domain.usecase.auth.CompleteProfileDetailsUseCase
import com.example.gamebuddy.domain.usecase.auth.GamesUseCase
import com.example.gamebuddy.domain.usecase.auth.KeywordsUseCase
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.isMessageExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val gamesUseCase: GamesUseCase,
    private val keywordsUseCase: KeywordsUseCase,
    private val completeProfileDetailsUseCase: CompleteProfileDetailsUseCase,
) : ViewModel() {

    private val _detailsUiState: MutableLiveData<UserDetailsState> =
        MutableLiveData(UserDetailsState())
    val detailsUiState: MutableLiveData<UserDetailsState> get() = _detailsUiState

    fun onTriggerEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.OnSetAge -> onSetAge(age = event.age)
            is DetailsEvent.OnSetAvatar -> onSetAvatar(avatar = event.avatar)
            is DetailsEvent.OnSetCountry -> onSetCountry(country = event.country)
            is DetailsEvent.OnSetGender -> onSetGender(gender = event.gender)
            DetailsEvent.GetGames -> getGames()
            DetailsEvent.GetKeywords -> getKeywords()
            is DetailsEvent.AddGameToSelected -> addGameToSelected(id = event.id)
            is DetailsEvent.AddKeywordToSelected -> addKeywordToSelected(id = event.id)
            DetailsEvent.SendProfileDetail -> sendProfileDetails()
            DetailsEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
        }
    }

    private fun sendProfileDetails() {
        val age = _detailsUiState.value?.age
        val gender = _detailsUiState.value?.gender
        val avatar = _detailsUiState.value?.avatar
        val country = _detailsUiState.value?.country
        val selectedGames = _detailsUiState.value?.selectedGames
        val selectedKeywords = _detailsUiState.value?.selectedKeywords

        val profileDetailsRequest = SetProfileDetailsRequest(
            age = age?.toInt() ?: 0,
            avatar = avatar ?: "",
            country = country ?: "",
            gender = gender ?: "",
            favoriteGames = selectedGames ?: listOf(),
            keywords = selectedKeywords ?: listOf()
        )

        completeProfileDetailsUseCase.execute(profileDetailsRequest)
            .onEach { dataState ->
                Timber.d("startup-logic: Collecting data: $dataState")
                _detailsUiState.value = detailsUiState.value?.copy(isLoading = dataState.isLoading)

                dataState?.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }

                dataState?.data?.let { data ->
                    _detailsUiState.value = detailsUiState.value?.copy(isProfileSetupDone = true)
                }


            }.launchIn(viewModelScope)

    }

    private fun addKeywordToSelected(id: String) {
        _detailsUiState.value?.let { state ->
            _detailsUiState.value = state.copy(selectedKeywords = state.selectedKeywords + id)
            Timber.d("Selected keywords: ${_detailsUiState.value!!.selectedKeywords}")
        }
    }

    private fun addGameToSelected(id: String) {
        _detailsUiState.value?.let { state ->
            _detailsUiState.value = state.copy(selectedGames = state.selectedGames + id)
            Timber.d("Selected games: ${_detailsUiState.value!!.selectedGames}")
        }
    }

    private fun onSetGender(gender: String) {
        _detailsUiState.value?.let { state ->
            _detailsUiState.value = state.copy(gender = gender)
        }
    }

    private fun onSetCountry(country: String) {
        _detailsUiState.value?.let { state ->
            _detailsUiState.value = state.copy(country = country)
        }
    }

    private fun onSetAvatar(avatar: String) {
        _detailsUiState.value?.let { state ->
            _detailsUiState.value = state.copy(avatar = avatar)
        }
    }

    private fun onSetAge(age: String) {
        _detailsUiState.value?.let { state ->
            _detailsUiState.value = state.copy(age = age)
        }
    }

    private fun removeHeadFromQueue() {
        _detailsUiState.value?.let {
            try {
                val queue = it.queue
                queue.remove()
                _detailsUiState.value = it.copy(queue = queue)
                Timber.d("Queue count after remove head: $_detailsUiState")
            } catch (e: Exception) {
                Timber.d("Nothing to remove ${e.message}")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage) {
        _detailsUiState.value?.let { state ->
            val queue = state.queue
            if (!stateMessage.isMessageExistInQueue(queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    Timber.d("RegisterViewModel Something added to queue: ${state.queue}")
                    _detailsUiState.value = state.copy(queue = queue)
                }
            }
        }
    }

    private fun getGames() {
        _detailsUiState.value?.let { state ->
            gamesUseCase.execute().onEach { dataState ->
                Timber.d("DetailsViewModel getGames: ${dataState.data}")

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }

                _detailsUiState.value = state.copy(games = dataState.data)
            }.launchIn(viewModelScope)
        }
    }

    private fun getKeywords() {
        _detailsUiState.value.let { state ->
            keywordsUseCase.execute().onEach { dataState ->

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }

                _detailsUiState.value = state?.copy(keywords = dataState.data?.body?.data?.keywords)
            }.launchIn(viewModelScope)
        }
    }

}