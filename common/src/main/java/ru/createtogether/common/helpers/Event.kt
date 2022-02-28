package ru.createtogether.common.helpers

class Event<out T>(val status: Status, val data: T?, val error: Int?, val e: Throwable? = null) {
    companion object {
        fun <T> loading(): Event<T> {
            return Event(Status.LOADING, null, null)
        }

        fun <T> success(data: T? = null): Event<T> {
            return Event(Status.SUCCESS, data, null)
        }

        fun <T> error(e: Throwable): Event<T> {
            return Event(Status.ERROR, null, null, e)
        }

        fun <T> error(error: Int): Event<T> {
            return Event(Status.ERROR, null, null)
        }
    }
}