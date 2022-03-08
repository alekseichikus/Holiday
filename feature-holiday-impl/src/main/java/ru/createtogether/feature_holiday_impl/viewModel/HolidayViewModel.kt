package ru.createtogether.feature_holiday_impl.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.Event
import ru.createtogether.feature_day_utils.model.DayModel
import ru.createtogether.feature_holiday_impl.data.HolidayRepository
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(
    private val holidayRepository: HolidayRepository
) : ViewModel() {

    var holidaysOfDayResponse = MutableLiveData<Event<List<HolidayModel>>>()
    fun loadHolidaysOfDay(date: String) {
        viewModelScope.launch {
            runCatching {
                holidayRepository.loadHolidays(date = date).collect {
                    holidaysOfDayResponse.postValue(it)
                }
            }.onFailure { throwable ->
                holidaysOfDayResponse.postValue(Event.error(throwable = throwable))
            }
        }
    }

    var holidaysByIdResponse = MutableLiveData<Event<List<HolidayModel>>>()
    fun loadHolidaysById(holidays: Array<Int>) {
        viewModelScope.launch {
            runCatching {
                holidayRepository.loadHolidaysById(holidays = holidays).collect {
                    holidaysByIdResponse.postValue(it)
                }
            }.onFailure { throwable ->
                holidaysByIdResponse.postValue(Event.error(throwable = throwable))
            }
        }
    }

    var nextDateWithHolidaysResponse = MutableLiveData<Event<DayModel>>()
    fun loadNextDateWithHolidays(date: String) {
        viewModelScope.launch {
            runCatching {
                holidayRepository.loadNextDateWithHolidays(date = date).collect {
                    nextDateWithHolidaysResponse.postValue(it)
                }
            }.onFailure { throwable ->
                nextDateWithHolidaysResponse.postValue(Event.error(throwable = throwable))
            }
        }
    }

    var holidaysOfMonth = MutableLiveData<Event<List<DayModel>>>()
    fun loadHolidaysOfMonth(date: String) {
        viewModelScope.launch {
            runCatching {
                holidayRepository.loadHolidaysOfMonth(date = date).collect {
                    holidaysOfMonth.postValue(it)
                }
            }.onFailure { throwable ->
                holidaysOfMonth.postValue(Event.error(throwable = throwable))
            }
        }
    }

    fun getFavorites() = holidayRepository.getFavorites()

    fun addHolidayLike(id: Int) {
        holidayRepository.addFavorite(id)
    }

    fun removeFavorite(id: Int) {
        holidayRepository.removeFavorite(id)
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