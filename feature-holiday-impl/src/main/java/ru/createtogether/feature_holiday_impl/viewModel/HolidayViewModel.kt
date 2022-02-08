package ru.createtogether.feature_holiday_impl.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.Event
import ru.createtogether.feature_holiday_impl.domain.HolidayRepository
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(
    private val holidayRepository: HolidayRepository
) : ViewModel() {

    var holidaysOfDayResponse = MutableLiveData<Event<List<HolidayModel>>>()
    fun loadHolidaysOfDay(date: String) {
        holidaysOfDayResponse.postValue(Event.loading())
        viewModelScope.launch {
            holidayRepository.loadHolidays(date = date).collect {
                it.data?.forEach {

                }
                holidaysOfDayResponse.postValue(it)
            }
        }
    }

}