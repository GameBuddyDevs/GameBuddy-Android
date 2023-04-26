package com.example.gamebuddy.data.remote.model.users

import com.google.gson.annotations.SerializedName

data class UsersData(
    @SerializedName("recommendedGamers")
    val userDto: List<UserDto>
)