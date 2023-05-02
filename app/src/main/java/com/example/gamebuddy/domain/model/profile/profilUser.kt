package com.example.gamebuddy.domain.model.profile

data class profilUser(
    val username:String,
    val age:String,
    val avatar: String? = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fgta1.jpg?alt=media&token=203ddbfa-79e1-4eaf-9b2e-7fbed362b7be",
    val games: List<String>,
    val keywords: List<String>,
    val joinedCommunities:List<String>,
    val friendsCount:Int
)