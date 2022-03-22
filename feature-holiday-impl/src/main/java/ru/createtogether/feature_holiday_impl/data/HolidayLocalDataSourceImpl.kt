package ru.createtogether.feature_holiday_impl.data

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.createtogether.feature_cache_impl.helpers.Preferences
import javax.inject.Inject

class HolidayLocalDataSourceImpl @Inject constructor(@ApplicationContext context: Context) :
    HolidayLocalDataSource {
    companion object {
        const val PREF_HOLIDAY_FAVORITES = "pref_holiday_favorites"
        const val PREF_NEXT_DAY_WITH_HOLIDAYS = "pref_next_day_with_holidays"
        const val PREF_NOTIFY_ABOUT_HOLIDAYS = "pref_notify_about_holidays"

        private const val DEFAULT_NOTIFY_ABOUT_HOLIDAYS = true
    }

    private val prefs =
        context.applicationContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    override var nextDateWithHolidays by Preferences.StringPreference(
        prefs,
        PREF_NEXT_DAY_WITH_HOLIDAYS, null
    )
    override var isNotifyAboutHolidays by Preferences.BooleanPreference(
        prefs,
        PREF_NOTIFY_ABOUT_HOLIDAYS, DEFAULT_NOTIFY_ABOUT_HOLIDAYS
    )

    override fun getFavorites(): Array<Int> =
        Gson().fromJson(prefs.getString(PREF_HOLIDAY_FAVORITES, "[]"), Array<Int>::class.java)

    override fun addFavorite(holidayId: Int) {
        with(getFavorites().toMutableList()) {
            if (indexOf(holidayId) == Constants.UNDEFINED)
                add(holidayId)
            prefs.edit().putString(PREF_HOLIDAY_FAVORITES, Gson().toJson(this)).apply()
        }
    }

    override fun isFavorite(holidayId: Int) = getFavorites().contains(holidayId)

    override fun removeFavorite(holidayId: Int) {
        with(getFavorites().toMutableList()) {
            remove(holidayId)
            prefs.edit().putString(PREF_HOLIDAY_FAVORITES, Gson().toJson(this)).apply()
        }
    }
}