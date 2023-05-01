package com.example.gamebuddy.data.remote.model.users

import com.example.gamebuddy.domain.model.user.User

data class UserDto(
    val userId: String,
    val gamerUsername: String,
    val age:Int,
    val country: String,
    val gender: String,
    val avatar: String,
    val favoriteGames:List<favoriteGames>,
    val selectedKeywords:List<String>
) {
    fun toUser(): User{
        return User(
            userId = userId,
            gamerUsername = gamerUsername,
            age = age,
            country = country,
            avatar = avatar,
            games = favoriteGames.map { it.gameName },
            keywords = selectedKeywords
        )
    }
}