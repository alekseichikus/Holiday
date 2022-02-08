package ru.createtogether.common.helpers

import ru.createtogether.common.helpers.Status

class Event<out T>(val status: Status, val data: T?, val error: Int?) {
    companion object {
        fun <T> loading(): Event<T> {
            return Event(Status.LOADING, null, null)
        }

        fun <T> success(data: T? = null): Event<T> {
            return Event(Status.SUCCESS, data, null)
        }

        fun <T> error(cause: Int): Event<T> {
            return Event(Status.ERROR, null, cause)
        }
    }
}