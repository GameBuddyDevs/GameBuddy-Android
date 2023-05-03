package com.example.gamebuddy.presentation.main.market

sealed class MarketEvent {

    data class UpdateQuery(val query: String) : MarketEvent()
    object NewQuery : MarketEvent()
    object NewSearch : MarketEvent()
    object OnRemoveHeadFromQueue : MarketEvent()
    object GetAvatars : MarketEvent()

}