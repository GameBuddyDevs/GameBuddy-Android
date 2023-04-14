package com.example.gamebuddy.presentation.auth.details.detailsGames

sealed class GamesEvent {
    data class Games(
        val games: String,
    ) : GamesEvent()

    object OnRemoveHeadFromQueue : GamesEvent()

}