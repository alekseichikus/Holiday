package ru.createtogether.feature_holiday_impl.viewModel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.Status
import ru.createtogether.common.helpers.extension.exceptionProcessing
import ru.createtogether.common.helpers.extension.withPattern
import ru.createtogether.feature_day_utils.model.DayOfMonthModel
import ru.createtogether.feature_day_utils.model.NextDayModel
import ru.createtogether.feature_holiday_impl.R
import ru.createtogether.feature_holiday_impl.data.HolidayRepository
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BaseHolidayViewModel @Inject constructor(
    private val holidayRepository: HolidayRepository
) : ViewModel() {
    private var _holidaysOfDayResponse =
        MutableStateFlow<Event<List<HolidayModel>>>(Event.loading())
    val holidaysOfDayResponse: StateFlow<Event<List<HolidayModel>>> = _holidaysOfDayResponse

    private var _holidaysByIdResponse = MutableStateFlow<Event<List<HolidayModel>>>(Event.loading())
    val holidaysByIdResponse: StateFlow<Event<List<HolidayModel>>> = _holidaysByIdResponse

    private var _nextDateWithHolidaysResponse =
        MutableStateFlow<Event<NextDayModel>>(Event.loading())
    val nextDateWithHolidaysResponse: StateFlow<Event<NextDayModel>> = _nextDateWithHolidaysResponse

    private var _holidaysOfMonthResponse =
        MutableStateFlow<Event<List<DayOfMonthModel>>>(Event.loading())
    val holidaysOfMonthResponse: StateFlow<Event<List<DayOfMonthModel>>> = _holidaysOfMonthResponse

    var nextDayWithHolidays: String?
        get() = holidayRepository.nextDateWithHolidays
        set(value) {
            holidayRepository.nextDateWithHolidays = value
        }

    var isNotifyAboutHolidays: Boolean
        get() = holidayRepository.isNotifyAboutHolidays
        set(value) {
            holidayRepository.isNotifyAboutHolidays = value
        }

    fun loadHolidaysOfDay(date: Date) {
        _holidaysOfDayResponse.value = Event.loading()
        viewModelScope.launch {
            holidayRepository.loadHolidays(date = date.withPattern(Constants.DEFAULT_DATE_PATTERN))
                .exceptionProcessing(_holidaysOfDayResponse)
        }
    }

    fun loadHolidaysById(holidaysId: Array<Int>) {
        _holidaysByIdResponse.value = Event.loading()
        viewModelScope.launch {
            holidayRepository.loadHolidaysById(holidaysId = holidaysId)
                .exceptionProcessing(_holidaysByIdResponse)
        }
    }

    fun setHolidaysById(holidays: Event<List<HolidayModel>>){
        _holidaysByIdResponse.value = holidays
    }

    fun loadNextDateWithHolidays(date: Date) {
        _nextDateWithHolidaysResponse.value = Event.loading()
        viewModelScope.launch {
            holidayRepository.loadNextDateWithHolidays(date = date.withPattern(Constants.DEFAULT_DATE_PATTERN))
                .exceptionProcessing(_nextDateWithHolidaysResponse)
        }
    }

    fun loadHolidaysOfMonth(date: String) {
        _holidaysOfMonthResponse.value = Event.loading()
        viewModelScope.launch {
            holidayRepository.loadHolidaysOfMonth(date = date)
                .exceptionProcessing(_holidaysOfMonthResponse)
        }
    }

    fun getFavorites() = holidayRepository.getFavorites()

    fun addFavorite(id: Int) {
        holidayRepository.addFavorite(id)
    }

    fun removeFavorite(id: Int) {
        holidayRepository.removeFavorite(id)
    }

    fun setFavorite(holiday: HolidayModel) {
        with(holiday) {
            if (isLike)
                addFavorite(holiday.id)
            else
                removeFavorite(holiday.id)
        }
    }
}