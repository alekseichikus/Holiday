package ru.createtogether.common.helpers

import androidx.lifecycle.MutableLiveData

interface ViewModelActions {
    val snackBarResponse: MutableLiveData<Event<Int>>
}