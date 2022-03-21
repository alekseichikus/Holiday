package ru.createtogether.fragment_main.presenter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import java.util.*

interface IMainFragment {
    fun initDataBinding()
    fun configureViews()
    fun initListeners()

    fun onMenuClick()
    fun onSearchClick()
    fun onCalendarClick()
    fun onFavoriteClick()
    fun onGoBackClick()
    fun onHolidayClick()

    fun setHolidaysOfCurrentDayEmptyListener()
    fun setInfoBoardViewListener()
    fun setHolidayViewListener()
    fun setCalendarResult()

    fun initObservers()
    fun observeLoadHolidaysOfDay()
    fun observeLoadNextDayWithHolidays()

    fun setDate(date: Date)

    fun showShimmers()
    fun hideShimmers()

    fun hideContent()
    fun setContent(holidays: List<HolidayModel>, date: Date? = null)

    fun initHolidaysShortAdapter(holidays: List<HolidayModel>)

    fun blockUI()
    fun unBlockUI()

    fun showInfoBoard(
        title: String,
        text: String,
        @DrawableRes icon: Int? = null,
        @StringRes titleButton: Int? = null
    )
    fun showInfoBoardSupportError()
    fun showInfoBoardInternetError()
}