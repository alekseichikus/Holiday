package ru.createtogether.feature_holiday_impl.data

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.createtogether.feature_cache_impl.data.PreferenceStorageImpl
import ru.createtogether.feature_cache_impl.helpers.Preferences
import javax.inject.Inject

class HolidayLocalDataSourceImpl @Inject constructor(@ApplicationContext context: Context) : HolidayLocalDataSource {
    private val prefs =
        context.applicationContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    override fun getFavorites(): Array<Int> =
        Gson().fromJson(prefs.getString(PREF_HOLIDAY_FAVORITES, "[]"), Array<Int>::class.java)

    override fun addFavorite(id: Int) {
        with(getFavorites().toMutableList()){
            if (indexOf(id) == Constants.UNDEFINED)
                add(id)
            prefs.edit().putString(PREF_HOLIDAY_FAVORITES, Gson().toJson(this)).apply()
        }
    }

    override fun isFavorite(holiday: Int) = getFavorites().contains(holiday)

    override fun removeFavorite(id: Int) {
        with(getFavorites().toMutableList()){
            remove(id)
            prefs.edit().putString(PREF_HOLIDAY_FAVORITES, Gson().toJson(this)).apply()
        }
    }

    override var nextDateWithHolidays by Preferences.StringPreference(prefs,
        PREF_NEXT_DAY_WITH_HOLIDAYS, null)
    override var isNotifyAboutHolidays by Preferences.BooleanPreference(prefs,
        PREF_NOTIFY_ABOUT_HOLIDAYS, true)

    companion object {
        const val PREF_HOLIDAY_FAVORITES = "pref_holiday_favorites"
        const val PREF_NEXT_DAY_WITH_HOLIDAYS = "pref_next_day_with_holidays"
        const val PREF_NOTIFY_ABOUT_HOLIDAYS = "pref_notify_about_holidays"
    }
}