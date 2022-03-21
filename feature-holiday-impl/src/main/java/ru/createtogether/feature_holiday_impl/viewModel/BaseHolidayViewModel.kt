package ru.createtogether.feature_holiday_impl.viewModel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.extension.exceptionProcessing
import ru.createtogether.common.helpers.extension.withPattern
import ru.createtogether.feature_day_utils.model.DayOfMonthModel
import ru.createtogether.feature_day_utils.model.NextDayModel
import ru.createtogether.feature_holiday_impl.data.HolidayRepository
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BaseHolidayViewModel @Inject constructor(
    private val holidayRepository: HolidayRepository
) : ViewModel() {

    var holidaysOfDayResponse = MutableLiveData<Event<List<HolidayModel>>>()
    fun loadHolidaysOfDay(date: Date) {
        holidaysOfDayResponse.postValue(Event.loading())
        viewModelScope.launch {
            holidayRepository.loadHolidays(date = date.withPattern(Constants.DEFAULT_DATE_PATTERN)).exceptionProcessing(holidaysOfDayResponse)
        }
    }

    var holidaysByIdResponse = MutableLiveData<Event<List<HolidayModel>>>()
    fun loadHolidaysById(holidaysId: Array<Int>) {
        holidaysByIdResponse.postValue(Event.loading())
        viewModelScope.launch {
            holidayRepository.loadHolidaysById(holidaysId = holidaysId)
                .exceptionProcessing(holidaysByIdResponse)
        }
    }

    var nextDateWithHolidaysResponse = MutableLiveData<Event<NextDayModel>>()
    fun loadNextDateWithHolidays(date: Date) {
        nextDateWithHolidaysResponse.postValue(Event.loading())
        viewModelScope.launch {
            holidayRepository.loadNextDateWithHolidays(date = date.withPattern(Constants.DEFAULT_DATE_PATTERN))
                .exceptionProcessing(nextDateWithHolidaysResponse)
        }
    }

    var holidaysOfMonth = MutableLiveData<Event<List<DayOfMonthModel>>>()
    fun loadHolidaysOfMonth(date: String) {
        holidaysOfMonth.postValue(Event.loading())
        viewModelScope.launch {
            holidayRepository.loadHolidaysOfMonth(date = date).exceptionProcessing(holidaysOfMonth)
        }
    }

    fun getFavorites() = holidayRepository.getFavorites()

    fun addHolidayLike(id: Int) {
        holidayRepository.addFavorite(id)
    }

    fun removeFavorite(id: Int) {
        holidayRepository.removeFavorite(id)
    }

    fun setFavorite(holiday: HolidayModel){
        with(holiday){
            if (isLike)
                addHolidayLike(holiday.id)
            else
                removeFavorite(holiday.id)
        }
    }

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
}