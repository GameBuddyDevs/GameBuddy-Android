package com.example.gamebuddy.presentation.auth.details

import com.example.gamebuddy.data.remote.model.keyword.Keyword
import com.example.gamebuddy.data.remote.model.keyword.KeywordResponse
import com.example.gamebuddy.domain.model.game.Game
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class UserDetailsState(
    val isLoading: Boolean = false,
    val games: List<Game>? = null,
    val keywords: List<Keyword>? = null,
    val age: String = "",
    val country: String = "",
    val avatar: String = "",
    val gender: String = "",
    val selectedGames: List<String> = listOf(),
    val selectedKeywords: List<String> = listOf(),
    val currentFragment: Int = 0,
    val isProfileSetupDone: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
