package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.remote.model.games.GamesResponse
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import timber.log.Timber

class GamesUseCase(
    private val service: GameBuddyApiAuthService,
){
    suspend fun execute():GamesResponse{
        val gamesResponse = service.getGames()
        if(!gamesResponse.status.success){
            Timber.e("gamesResponse Case errorrr: ${gamesResponse.status.message}")
            throw Exception(gamesResponse.status.message)
        }
        Timber.d("Games Use Case success: ${gamesResponse.body.gamesData.gameName}")
        return gamesResponse
    }
}