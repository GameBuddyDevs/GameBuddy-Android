package com.example.gamebuddy.presentation.auth.details.detailsUser


sealed class DetailsUserEvent {
    data class DetailsUser(
        val age: String,
        val country: String,
        val avatar: String,
        val gender: String,
    ) : DetailsUserEvent()

    object OnRemoveHeadFromQueue : DetailsUserEvent()

}