package com.example.gamebuddy.presentation.main.profile

sealed class ProfileEvent{
    object GetUserInfo: ProfileEvent()
    object OnRemoveHeadFromQueue : ProfileEvent()
}