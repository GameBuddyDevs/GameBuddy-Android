package com.example.gamebuddy.data.remote.model.avatar

import com.example.gamebuddy.domain.model.avatar.Avatar

data class AvatarDto (
    val id: String,
    val image: String,
){
    fun toAvatar(): Avatar {
        return Avatar(
            id = id,
            image = image,
        )
    }
}