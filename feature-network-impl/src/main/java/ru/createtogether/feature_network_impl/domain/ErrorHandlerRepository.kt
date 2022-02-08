package ru.createtogether.feature_network_impl.domain

interface ErrorHandlerRepository {
    fun handleErrorMessage(errorMessage: String?): Int

    fun handleErrorResponse(e: Throwable): Int
}