package com.example.gamebuddy.presentation.main.profile

sealed class AllFriendsEvent {
    data class OnSetUserId(val userId:String):AllFriendsEvent()
    object GetAllFriends: AllFriendsEvent()
    object OnRemoveHeadFromQueue :AllFriendsEvent()
    object RemoveFriend: AllFriendsEvent()
    object ResetAllFriends: AllFriendsEvent()
}