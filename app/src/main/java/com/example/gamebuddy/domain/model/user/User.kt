package com.example.gamebuddy.domain.model.user

data class User(
    val userId:String ,
    val gamerUsername: String? = null,
    val age: Int? = null,
    val country: String? = null,
    val games: List<String>,
    val keywords: List<String>,
    val avatar: String? = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fgta1.jpg?alt=media&token=203ddbfa-79e1-4eaf-9b2e-7fbed362b7be"
)