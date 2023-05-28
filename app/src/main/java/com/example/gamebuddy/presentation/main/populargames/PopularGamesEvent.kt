package com.example.gamebuddy.presentation.main.populargames


sealed class PopularGamesEvent {

    object GetGames : PopularGamesEvent()

    object OnRemoveHeadFromQueue : PopularGamesEvent()
}