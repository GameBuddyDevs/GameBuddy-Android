package com.example.gamebuddy.presentation.main.match

sealed class MatchEvent {
    object GetUsers: MatchEvent()
    object OnRemoveHeadFromQueue : MatchEvent()
}