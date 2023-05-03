package com.example.gamebuddy.data.remote.model.market

import com.example.gamebuddy.domain.model.market.Market
import com.example.gamebuddy.data.remote.model.Status


class MarketResponse (
    val body: Body,
    val status: Status
) {
    fun toMarket(): List<Market> {
        return body.data.specialAvatarsDto.map { it.toMarket() }
    }
}