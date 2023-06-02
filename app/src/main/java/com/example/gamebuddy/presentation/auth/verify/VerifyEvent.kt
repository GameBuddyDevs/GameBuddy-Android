package com.example.gamebuddy.presentation.auth.verify

sealed class VerifyEvent {

    /**
     * Can be used for updating the verification code also.
     * */
    data class EnteredVerificationCode(
        val verificationCode: String
    ): VerifyEvent()

    object OnRemoveHeadFromQueue: VerifyEvent()
}