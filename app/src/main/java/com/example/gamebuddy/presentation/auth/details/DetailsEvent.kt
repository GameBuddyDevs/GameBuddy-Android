package com.example.gamebuddy.presentation.auth.details


sealed class DetailsEvent {

    data class OnSetAge(val age: String): DetailsEvent()

    data class OnSetCountry(val country: String): DetailsEvent()

    data class OnSetAvatar(val avatar: String): DetailsEvent()

    data class OnSetGender(val gender: String): DetailsEvent()

    object GetGames : DetailsEvent()

    object GetKeywords: DetailsEvent()

    object SendProfileDetail : DetailsEvent()

    data class AddGameToSelected(val id: String) : DetailsEvent()

    data class AddKeywordToSelected(val id: String) : DetailsEvent()

    object OnRemoveHeadFromQueue : DetailsEvent()
}