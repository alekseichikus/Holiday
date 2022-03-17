package ru.createtogether.common.helpers.baseFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.ViewModelActions

open class BaseViewModel: ViewModel(), ViewModelActions {
    override val snackBarResponse = MutableLiveData<Event<Int>>()
}