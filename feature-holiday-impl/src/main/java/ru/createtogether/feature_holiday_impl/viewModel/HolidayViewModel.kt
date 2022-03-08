package ru.createtogether.feature_holiday_impl.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.Status
import ru.createtogether.feature_cache_impl.domain.PreferenceStorage
import ru.createtogether.feature_day_utils.model.DayModel
import ru.createtogether.feature_holiday_impl.data.HolidayRepository
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(private val holidayRepository: HolidayRepository
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
            holidayRepository.loadHolidaysByIds(holidays).collect {
                it.data?.forEach {
                    it.isLike = holidayRepository.isFavorite(it.id)
                }
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

    var holidaysOfMonth = MutableLiveData<Event<List<DayModel>>>()
    fun loadHolidaysOfMonth(dates: List<String>) {
        viewModelScope.launch {
            holidaysOfMonth.postValue(Event.loading())
            val months = mutableListOf<DayModel>()

            var isError = false
            dates.forEach { date ->
                holidayRepository.loadHolidaysOfMonth(date = date).collect {
                    if (it.status == Status.SUCCESS && it.data.isNullOrEmpty().not())
                        months.addAll(it.data!!)
                    else if (it.status == Status.ERROR)
                        isError = true
                }
            }
            if (isError)
                holidaysOfMonth.postValue(Event.error(0))
            else
                holidaysOfMonth.postValue(Event.success(months))
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