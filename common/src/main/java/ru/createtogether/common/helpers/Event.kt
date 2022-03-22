package ru.createtogether.common.helpers

class Event< T>(val status: Status, val data: T?, val throwable: Throwable? = null) {
    companion object {
        fun <T> loading(): Event<T> {
            return Event(Status.LOADING, null, null)
        }

        fun <T> success(data: T? = null): Event<T> {
            return Event(Status.SUCCESS, data, null)
        }

        fun <T> error(cause: Int? = null, throwable: Throwable? = null): Event<T> {
            return Event(Status.ERROR, null, throwable)
        }
    }
}