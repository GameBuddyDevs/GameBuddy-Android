package com.example.gamebuddy.presentation.main.gamedetail


sealed class GameDetailEvent {

    data class GetGameDetail(val gameId: String) : GameDetailEvent()

    object OnRemoveHeadFromQueue : GameDetailEvent()
}