package com.example.feature_app_impl.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.createtogether.feature_cache_impl.helpers.Preferences
import javax.inject.Inject

class AppLocalDataSourceImpl @Inject constructor(@ApplicationContext context: Context) :
    AppLocalDataSource {
    private val prefs =
        context.applicationContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREF_APP_VERSION_CODE = "pref_app_version_code"
    }

    override var versionCode by Preferences.StringPreference(
        prefs,
        PREF_APP_VERSION_CODE, null)
}