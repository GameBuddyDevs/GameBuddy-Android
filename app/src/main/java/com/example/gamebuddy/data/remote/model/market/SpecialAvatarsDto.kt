package com.example.gamebuddy.data.remote.model.market

import com.example.gamebuddy.domain.model.market.Market

class SpecialAvatarsDto(
    val id: String,
    val image: String,
    val price: String

) {
    fun toMarket(): Market {
        return Market(
            id = id,
            image = image,
            price = price,
        )
    }
}