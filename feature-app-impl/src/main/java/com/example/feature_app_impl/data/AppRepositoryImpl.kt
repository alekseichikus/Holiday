package com.example.feature_app_impl.data

import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val appLocalDataSource: AppLocalDataSource
) : AppRepository {
    override var versionCode = appLocalDataSource.versionCode
}