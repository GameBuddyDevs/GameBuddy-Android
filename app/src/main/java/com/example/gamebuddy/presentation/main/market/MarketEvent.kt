package com.example.gamebuddy.presentation.main.market

sealed class MarketEvent {
    data class OnSetAvatarId(val avatarId:String):MarketEvent()
    object OnRemoveHeadFromQueue : MarketEvent()
    object GetAvatars : MarketEvent()
    object BuyItem: MarketEvent()
    object GetCoin: MarketEvent()

}