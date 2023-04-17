package com.example.gamebuddy.data.remote.request.setdetails

data class SetProfileDetailsRequest(
    val age: Int,
    val avatar: String,
    val country: String,
    val gender: String,
    val favoriteGames: List<String>,
    val keywords: List<String>
)