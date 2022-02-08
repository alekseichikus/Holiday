package ru.createtogether.fragment_main.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    var currentDate: Date = Calendar.getInstance().time
    val nowDate: Date = currentDate.clone() as Date
    var currentMoveToDate: Date? = null
}