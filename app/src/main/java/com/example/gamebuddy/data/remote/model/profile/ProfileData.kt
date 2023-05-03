package com.example.gamebuddy.data.remote.model.profile

import com.example.gamebuddy.domain.model.profile.profilUser
import com.google.gson.annotations.SerializedName

data class ProfileData(
    val username:String,
    val email:String,
    val age:String,
    val country:String,
    val avatar:String,
    val gender:String,
    val coin:Int,
    val games:List<profileGames>,
    val keywords:List<profileKeywords>,
    val achievements:List<profileAchievements>,
    val joinedCommunities:List<profileJoinedCommunities>,
    val friends:List<profileFriends>
){
    fun toProfileUser():profilUser{
        return profilUser(
            username = username,
            age = age,
            avatar = avatar,
            games = games.map { it.gameName },
            keywords = keywords.map { it.keywordName },
            joinedCommunities = joinedCommunities.map { it.name },
            friendsCount = friends.size
        )
    }
}