package ru.createtogether.feature_holiday_impl.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.Event
import ru.createtogether.feature_cache_impl.domain.PreferenceStorage
import ru.createtogether.feature_day_utils.model.DayModel
import ru.createtogether.feature_holiday_impl.domain.HolidayRepository
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val holidayRepository: HolidayRepository
) : ViewModel() {

    var holidaysOfDayResponse = MutableLiveData<Event<List<HolidayModel>>>()
    fun loadHolidaysOfDay(date: String) {
        holidaysOfDayResponse.postValue(Event.loading())
        viewModelScope.launch {
            holidayRepository.loadHolidays(date = date).collect {
                it.data?.forEach {
                    it.isLike = preferenceStorage.isHolidayLike(it.id)
                }
                holidaysOfDayResponse.postValue(it)
            }
        }
    }

    var holidaysByIdResponse = MutableLiveData<Event<List<HolidayModel>>>()
    fun loadHolidaysById(holidays: Array<Int>) {
        viewModelScope.launch {
            holidayRepository.loadHolidaysByIds(holidays).collect {
                holidaysByIdResponse.postValue(it)
            }
        }
    }

    var nextDayWithHolidaysResponse = MutableLiveData<Event<DayModel>>()
    fun loadNextDateWithHolidays(date: String) {
        viewModelScope.launch {
            holidayRepository.loadNextDayWithHolidays(date = date).collect {
                nextDayWithHolidaysResponse.postValue(it)
            }
        }
    }

    var nextDateWithHolidaysResponse = MutableLiveData<Event<DayModel>>()
    fun loadNextDayWithHolidays(date: String) {
        viewModelScope.launch {
            holidayRepository.loadNextDayWithHolidays(date = date).collect {
                nextDateWithHolidaysResponse.postValue(it)
            }
        }
    }

    fun getHolidaysLike() = preferenceStorage.getHolidayLikes()

    fun addHolidayLike(id: Int) {
        preferenceStorage.addHolidayLikes(id)
    }

    fun removeHolidayLike(id: Int) {
        preferenceStorage.removeHolidayLikes(id)
    }

    var nextDayWithHolidays: String?
        get() = preferenceStorage.nextDayWithHolidays
        set(value) {
            preferenceStorage.nextDayWithHolidays = value
        }
}