package ru.createtogether.feature_cache_impl.data

import android.content.Context
import com.google.gson.Gson
import ru.createtogether.feature_cache_impl.domain.PreferenceStorage
import javax.inject.Inject

class PreferenceStorageImpl @Inject constructor(context: Context) : PreferenceStorage {

    companion object {
        const val PREFS_NAME = "ru.createTogether.holidays.prefs"

        const val PREF_HOLIDAY_FAVORITES = "pref_holiday_favorites"
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
        if(holidays.indexOf(id) == -1)
            holidays.add(id)
        prefs.edit().putString(PREF_HOLIDAY_FAVORITES, Gson().toJson(holidays)).apply()
    }

    override fun isHolidayLike(holiday: Int) = getHolidayLikes().contains(holiday)

    override fun removeHolidayLikes(id: Int) {
        val holidays = getHolidayLikes().toMutableList()
        holidays.remove(id)
        prefs.edit().putString(PREF_HOLIDAY_FAVORITES, Gson().toJson(holidays)).apply()
    }
}