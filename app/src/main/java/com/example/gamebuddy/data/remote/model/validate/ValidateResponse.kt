package com.example.gamebuddy.data.remote.model.validate

import com.example.gamebuddy.data.remote.model.Status

data class ValidateResponse(
    val body: ValidateBody,
    val status: Status
)