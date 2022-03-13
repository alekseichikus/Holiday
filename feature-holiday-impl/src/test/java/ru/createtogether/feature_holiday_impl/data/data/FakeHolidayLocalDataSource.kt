package ru.createtogether.feature_holiday_impl.data.data

import ru.createtogether.feature_holiday_impl.data.HolidayData
import ru.createtogether.feature_holiday_impl.data.HolidayLocalDataSource
import ru.createtogether.feature_holiday_utils.model.HolidayModel

class FakeHolidayLocalDataSource(var holidays: MutableList<HolidayModel>? = mutableListOf()) :
    HolidayLocalDataSource {
    override fun getFavorites(): Array<Int> {
        return HolidayData.getFavorites()
    }

    override fun addFavorite(holidayId: Int) {
        TODO("Not yet implemented")
    }

    override fun isFavorite(holidayId: Int): Boolean {
        return HolidayData.getFavorites().contains(holidayId)
    }

    override fun removeFavorite(holidayId: Int) {

    }

    override var nextDateWithHolidays: String? = ""
    override var isNotifyAboutHolidays: Boolean = true
}