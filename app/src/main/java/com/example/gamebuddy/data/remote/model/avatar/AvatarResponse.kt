package com.example.gamebuddy.data.remote.model.avatar

import com.example.gamebuddy.data.remote.model.Status
import com.example.gamebuddy.domain.model.avatar.Avatar

data class AvatarResponse (
    val body: Body,
    val status: Status
) {
    fun toAvatar(): List<Avatar> {
        return body.data.avatarDto.map { it.toAvatar() }
    }
}