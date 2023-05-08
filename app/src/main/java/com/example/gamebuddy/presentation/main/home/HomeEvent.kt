package com.example.gamebuddy.presentation.main.home


sealed class HomeEvent {
    data class OnSetUserId(val userId:String): HomeEvent()
    data class OnSetAcceptFriend(val accept:Boolean): HomeEvent()
    object GetPendingFriends: HomeEvent()
    object OnRemoveHeadFromQueue : HomeEvent()
    object AcceptFriends: HomeEvent()
    object ResetPendingFriends:HomeEvent()
}