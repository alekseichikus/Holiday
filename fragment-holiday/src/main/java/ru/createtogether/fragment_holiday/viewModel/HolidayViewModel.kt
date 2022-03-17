package ru.createtogether.fragment_holiday.viewModel

import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.baseFragment.BaseViewModel
import ru.createtogether.common.helpers.extension.getDateString
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.fragment_holiday.R

class HolidayViewModel : BaseViewModel() {

    fun setFavorite(isFavorite: Boolean) {
        snackBarResponse.value = Event.success(
            if (isFavorite)
                R.string.snack_add_to_favorite
            else
                R.string.snack_remove_from_favorite
        )
    }

    fun getShareText(holiday: HolidayModel) = StringBuilder().apply {
        appendLine(holiday.title)
        appendLine(
            getDateString(holiday.date, Constants.DEFAULT_DATE_PATTERN)
        )
        appendLine(holiday.description)
    }

    fun getCoordinatorScrollRatio(appBarHeight: Int, verticalOffset: Int) = 1 - (appBarHeight + verticalOffset) / appBarHeight.toFloat()
}