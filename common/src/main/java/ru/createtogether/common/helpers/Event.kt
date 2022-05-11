package ru.createtogether.common.helpers

sealed class Event<out T> {
    object Loading: Event<Nothing>()

    data class Success<T>(val data: T): Event<T>()

    data class Error(val throwable: Throwable): Event<Nothing>()
}