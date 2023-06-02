package com.example.gamebuddy.data.remote.model.market

import com.google.gson.annotations.SerializedName


data class MarketData(
    @SerializedName("specialAvatars")
    val specialAvatarsDto: List<SpecialAvatarsDto>
)