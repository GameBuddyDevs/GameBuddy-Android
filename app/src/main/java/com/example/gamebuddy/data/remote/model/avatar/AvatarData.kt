package com.example.gamebuddy.data.remote.model.avatar

import com.google.gson.annotations.SerializedName

data class AvatarData (
    @SerializedName("avatars")
    val avatarDto: List<AvatarDto>
)