package com.example.gamebuddy.util

enum class ApiType(
    val url: String
) {
    AUTH("l2.eren.wtf:4567/"),
    APPLICATION("l2.eren.wtf:4568/"),
    MATCH("l2.eren.wtf:4569/match/"),
    MESSAGE("l2.eren.wtf:4569/messages/"),
    COMMUNITY("l2.eren.wtf:4565/community/"),
}