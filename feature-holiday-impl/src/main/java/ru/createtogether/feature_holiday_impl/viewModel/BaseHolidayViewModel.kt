package ru.createtogether.feature_holiday_impl.viewModel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.Status
import ru.createtogether.common.helpers.extension.exceptionProcessing
import ru.createtogether.common.helpers.extension.withPattern
import ru.createtogether.feature_day_utils.model.DayOfMonthModel
import ru.createtogether.feature_day_utils.model.MonthModel
import ru.createtogether.feature_day_utils.model.NextDayModel
import ru.createtogether.feature_holiday_impl.R
import ru.createtogether.feature_holiday_impl.data.HolidayRepository
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import java.util.*
import javax.inject.Inject

class BaseHolidayViewModel @Inject constructor(
    private val holidayRepository: HolidayRepository
) : ViewModel() {
    private var _holidaysOfDayResponse =
        MutableStateFlow<Event<List<HolidayModel>>>(Event.Loading)
    val holidaysOfDayResponse = _holidaysOfDayResponse

    private var _holidaysByIdResponse = MutableStateFlow<Event<List<HolidayModel>>>(Event.Loading)
    val holidaysByIdResponse: StateFlow<Event<List<HolidayModel>>> = _holidaysByIdResponse

    private var _nextDateWithHolidaysResponse =
        MutableStateFlow<Event<NextDayModel>>(Event.Loading)
    val nextDateWithHolidaysResponse: StateFlow<Event<NextDayModel>> = _nextDateWithHolidaysResponse

    private var _holidaysOfMonthResponse =
        MutableStateFlow<Event<List<MonthModel>>>(Event.Loading)
    val holidaysOfMonthResponse: StateFlow<Event<List<MonthModel>>> = _holidaysOfMonthResponse

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
        _holidaysOfDayResponse.value = Event.Loading
        viewModelScope.launch {
            holidayRepository.loadHolidays(date = date.withPattern(Constants.DEFAULT_DATE_PATTERN))
                .exceptionProcessing(_holidaysOfDayResponse)
        }
    }

    fun loadHolidaysById(holidaysId: Array<Int>) {
        _holidaysByIdResponse.value = Event.Loading
        viewModelScope.launch {
            holidayRepository.loadHolidaysById(holidaysId = holidaysId)
                .exceptionProcessing(_holidaysByIdResponse)
        }
    }

    fun setHolidaysById(holidays: List<HolidayModel>) {
        _holidaysByIdResponse.value = Event.Success(data = holidays)
    }

    fun loadNextDateWithHolidays(date: Date) {
        _nextDateWithHolidaysResponse.value = Event.Loading
        viewModelScope.launch {
            holidayRepository.loadNextDateWithHolidays(date = date.withPattern(Constants.DEFAULT_DATE_PATTERN))
                .exceptionProcessing(_nextDateWithHolidaysResponse)
        }
    }

    fun loadHolidaysOfMonth(date: String, months: List<MonthModel>) {
        _holidaysOfMonthResponse.value = Event.Loading
        viewModelScope.launch {
            holidayRepository.loadHolidaysOfMonth(date = date).catch { throwable ->
                _holidaysOfMonthResponse.value = Event.Error(throwable = throwable)
            }.collect { days ->
                months.forEach { month ->
                    month.days.forEach { day ->
                        day.count =
                            days.find { it.dateString == day.calendar.time.withPattern(Constants.DEFAULT_DATE_PATTERN) }?.holidaysCount
                                ?: 0
                    }
                }
                _holidaysOfMonthResponse.value = Event.Success(data = months)
            }
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