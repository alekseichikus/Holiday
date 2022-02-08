package ru.createtogether.feature_network_impl.data

import ru.createtogether.feature_network_impl.domain.ErrorHandlerRepository
import ru.createtogether.feature_network_impl.helpers.NetworkConstants

class ErrorHandlerRepositoryImpl : ErrorHandlerRepository {
     override fun handleErrorMessage(errorMessage: String?): Int {
        return NetworkConstants.HTTP_INTERNAL_SERVER_ERROR
    }

    override fun handleErrorResponse(e: Throwable): Int {
        //httpException.code() == NetworkConstants.HTTP_BAD_REQUEST
        return NetworkConstants.HTTP_FORBIDDEN
    }
}