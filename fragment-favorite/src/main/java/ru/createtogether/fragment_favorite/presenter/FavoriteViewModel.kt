package ru.createtogether.fragment_favorite.presenter

import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.Status
import ru.createtogether.common.helpers.baseFragment.BaseViewModel
import ru.createtogether.feature_holiday_impl.R
import ru.createtogether.feature_holiday_utils.model.HolidayModel

class FavoriteViewModel : BaseViewModel() {
    fun updateSort(sortId: Int, holidays: Event<List<HolidayModel>>): Event<List<HolidayModel>> {
        with(holidays) {
            if (status == Status.SUCCESS) {
                data?.let {
                    return when (sortId) {
                        R.id.idName -> Event.success(it.sortedBy { holiday -> holiday.title })
                        else -> Event.success(it.sortedBy { holiday -> holiday.date })
                    }
                }
            }
        }
        return holidays
    }
}