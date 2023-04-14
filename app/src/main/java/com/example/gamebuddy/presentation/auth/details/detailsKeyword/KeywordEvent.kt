package com.example.gamebuddy.presentation.auth.details.detailsKeyword

sealed class KeywordEvent {
    data class Keyword(
        val keyword: String,
    ) : KeywordEvent()

    object OnRemoveHeadFromQueue : KeywordEvent()
}