package ru.createtogether.feature_cache_impl.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import ru.createtogether.feature_cache_impl.domain.PreferenceStorage
import ru.createtogether.feature_cache_impl.helpers.Preferences
import javax.inject.Inject

class PreferenceStorageImpl @Inject constructor(context: Context) : PreferenceStorage {

    companion object {
        const val PREFS_NAME = "ru.createTogether.holidays.prefs"
    }

    private val prefs =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}