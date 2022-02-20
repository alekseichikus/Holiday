package ru.createtogether.bottom_calendar.presenter.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.createtogether.birthday.imageCalendar.model.MonthModel
import ru.createtogether.bottom_calendar.domain.ImageCalendarRepository
import ru.createtogether.common.helpers.Event
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val imageCalendarRepository: ImageCalendarRepository) :
    ViewModel() {

    val loadCalendarsResponse = MutableLiveData<Event<List<MonthModel>>>()

    fun loadCalendars(curCalendar: Calendar, startingAt: Int, now: Calendar) {
        loadCalendarsResponse.value = Event.loading()
        viewModelScope.launch {
            imageCalendarRepository.loadCalendar(
                curCalendar = curCalendar,
                startingAt = startingAt,
                now = now
            ).collect {
                loadCalendarsResponse.postValue(it)
            }
        }
    }
}