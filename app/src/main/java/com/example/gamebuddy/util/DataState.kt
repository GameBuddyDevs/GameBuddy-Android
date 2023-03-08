package com.example.gamebuddy.util


/**
 * https://github.dev/mitchtabian/Open-API-Android-App/tree/master/app/src/main/java/com/codingwithmitch/openapi/presentation/main/blog
 * */

data class DataState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val stateMessage: StateMessage? = null,
) {

    companion object {

        fun <T> success(
            response: Response?,
            data: T? = null,
        ): DataState<T> {
            return DataState(
                stateMessage = response?.let { StateMessage(it) },
                data = data,
            )
        }

        fun <T> error(
            response: Response,
        ): DataState<T> {
            return DataState(
                stateMessage = StateMessage(response),
                data = null,
            )
        }

        fun <T> loading(): DataState<T> = DataState(isLoading = true)
    }
}