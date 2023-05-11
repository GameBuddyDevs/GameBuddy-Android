package com.example.gamebuddy.presentation.main.market

sealed class MarketEvent {
    object OnRemoveHeadFromQueue : MarketEvent()
    object GetAvatars : MarketEvent()

}