package com.example.gamebuddy.presentation.main.profile

import com.example.gamebuddy.data.remote.model.keyword.Keyword
import com.example.gamebuddy.domain.model.avatar.Avatar
import com.example.gamebuddy.domain.model.game.Game
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class EditState(
    val param: String ="", //which variable edited.
    val isLoading: Boolean = false,
    val games: List<Game>? = null,
    val keywords: List<Keyword>? = null,
    val username:String = "",
    val avatars: List<Avatar>? = listOf(),
    val age: String = "",
    val avatarId: String ="",
    val selectedGames: List<String> = listOf(),
    val selectedKeywords: List<String> = listOf(),
    val isProfileSetupDone: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)