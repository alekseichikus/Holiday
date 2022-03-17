package ru.createtogether.feature_holiday_impl.viewModel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.extension.exceptionProcessing
import ru.createtogether.feature_day_utils.model.DayModel
import ru.createtogether.feature_holiday_impl.data.HolidayRepository
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import javax.inject.Inject

@HiltViewModel
class BaseHolidayViewModel @Inject constructor(
    private val holidayRepository: HolidayRepository
) : ViewModel() {

    var holidaysOfDayResponse = MutableLiveData<Event<List<HolidayModel>>>()
    fun loadHolidaysOfDay(date: String) {
        viewModelScope.launch {
            holidayRepository.loadHolidays(date = date).exceptionProcessing(holidaysOfDayResponse)
        }
    }

    var holidaysByIdResponse = MutableLiveData<Event<List<HolidayModel>>>()
    fun loadHolidaysById(holidaysId: Array<Int>) {
        viewModelScope.launch {
            holidayRepository.loadHolidaysById(holidaysId = holidaysId)
                .exceptionProcessing(holidaysByIdResponse)
        }
    }

    var nextDateWithHolidaysResponse = MutableLiveData<Event<DayModel>>()
    fun loadNextDateWithHolidays(date: String) {
        viewModelScope.launch {
            holidayRepository.loadNextDateWithHolidays(date = date)
                .exceptionProcessing(nextDateWithHolidaysResponse)
        }
    }

    var holidaysOfMonth = MutableLiveData<Event<List<DayModel>>>()
    fun loadHolidaysOfMonth(date: String) {
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
            isLike = isLike.not()
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