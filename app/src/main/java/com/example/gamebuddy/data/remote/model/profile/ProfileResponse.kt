package com.example.gamebuddy.data.remote.model.profile
import com.example.gamebuddy.data.remote.model.Status
import com.example.gamebuddy.domain.model.profile.profilUser

data class ProfileResponse(
    val body:ProfileBody,
    val status:Status
){
    fun toProfileUser():profilUser?{
        return body?.data?.toProfileUser()
    }
    fun toCoin():Int{
        return body.data.toCoin()
    }
}