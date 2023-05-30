package com.example.gamebuddy.presentation.main.profile


sealed class EditEvent {
    data class OnSetAge(val age: String): EditEvent()
    data class OnSetAvatar(val avatarId: String): EditEvent()

    data class OnSetUsername(val username: String): EditEvent()

    data class AddGameToSelected(val id: String) : EditEvent()

    data class AddKeywordToSelected(val id: String) : EditEvent()

    data class OnSetParam(val param: String): EditEvent()

    object GetGames : EditEvent()

    object GetKeywords: EditEvent()

    object GetAvatars: EditEvent()

    object Edit : EditEvent()

    object OnRemoveHeadFromQueue : EditEvent()
}