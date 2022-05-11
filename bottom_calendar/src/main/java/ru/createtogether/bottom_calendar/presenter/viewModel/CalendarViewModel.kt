package ru.createtogether.bottom_calendar.presenter.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.createtogether.feature_day_utils.model.MonthModel
import ru.createtogether.bottom_calendar.domain.ImageCalendarRepository
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.extension.exceptionProcessing
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val imageCalendarRepository: ImageCalendarRepository) :
    ViewModel() {

    private var _loadCalendarsResponse = MutableStateFlow<Event<List<MonthModel>>>(Event.Loading)
    val loadCalendarsResponse: StateFlow<Event<List<MonthModel>>> = _loadCalendarsResponse

    fun loadCalendars(curCalendar: Calendar, startingAt: Int, now: Calendar) {
        _loadCalendarsResponse.value = Event.Loading
        viewModelScope.launch {
            imageCalendarRepository.loadCalendar(
                curCalendar = curCalendar,
                startingAt = startingAt,
                now = now
            ).exceptionProcessing(_loadCalendarsResponse)
        }
    }
}