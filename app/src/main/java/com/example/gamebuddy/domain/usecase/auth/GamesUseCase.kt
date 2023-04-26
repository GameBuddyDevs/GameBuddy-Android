package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.domain.model.game.Game
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GamesUseCase(
    private val service: GameBuddyApiAppService,
) {
    fun execute(): Flow<DataState<List<Game>>> = flow {
        Timber.d("Games Use Case: ")
        emit(DataState.loading())

        val games = service.getGames().toGames()


        Timber.d("Games Use Case success: ${games[0].name}")

        emit(DataState.success(response = null, data = games))
    }.catch {
        Timber.e("Games Use Case hatakeeee: $it")
        emit(handleUseCaseException(it))
    }
}