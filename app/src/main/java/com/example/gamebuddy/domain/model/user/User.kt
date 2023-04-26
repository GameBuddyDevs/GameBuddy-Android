package com.example.gamebuddy.domain.model.user

data class User(
    val userId:String ,
    val gamerUsername: String? = null,
    val age: Int? = null,
    val country: String? = null
)