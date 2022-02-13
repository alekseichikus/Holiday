package ru.createtogether.feature_cache_impl.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import ru.createtogether.feature_cache_impl.domain.PreferenceStorage
import javax.inject.Inject

class PreferenceStorageImpl @Inject constructor(context: Context) : PreferenceStorage {

    companion object {
        const val PREFS_NAME = "ru.createTogether.holidays.prefs"

        const val PREF_HOLIDAY_FAVORITES = "pref_holiday_favorites"
        const val PREF_APP_VERSION_CODE = "pref_app_version_code"
        const val PREF_NEXT_DAY_WITH_HOLIDAYS = "pref_next_day_with_holidays"
    }

    private val prefs =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getHolidayLikes(): Array<Int> {
        val gson = Gson()
        val string = prefs.getString(PREF_HOLIDAY_FAVORITES, "[]")
        return gson.fromJson(string, Array<Int>::class.java)
    }

    override fun addHolidayLikes(id: Int) {
        val holidays = getHolidayLikes().toMutableList()
        if (holidays.indexOf(id) == -1)
            holidays.add(id)
        prefs.edit().putString(PREF_HOLIDAY_FAVORITES, Gson().toJson(holidays)).apply()
    }

    override fun isHolidayLike(holiday: Int) = getHolidayLikes().contains(holiday)

    override fun removeHolidayLikes(id: Int) {
        val holidays = getHolidayLikes().toMutableList()
        holidays.remove(id)
        prefs.edit().putString(PREF_HOLIDAY_FAVORITES, Gson().toJson(holidays)).apply()
    }

    override var versionCode: String
        get() = prefs.getString(PREF_APP_VERSION_CODE, null).orEmpty()
        set(value) = prefs.edit().putString(PREF_APP_VERSION_CODE, value).apply()

    override var nextDayWithHolidays: String
        get() = prefs.getString(PREF_NEXT_DAY_WITH_HOLIDAYS, null).orEmpty()
        set(value) {
            Log.d("Sdasdasdsadsadasd", "dssdsdsd")
            prefs.edit().putString(PREF_NEXT_DAY_WITH_HOLIDAYS, value).apply()
        }
}