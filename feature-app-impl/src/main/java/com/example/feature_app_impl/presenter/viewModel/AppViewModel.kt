package com.example.feature_app_impl.presenter.viewModel

import com.example.feature_app_impl.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.createtogether.common.helpers.baseFragment.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val appRepository: AppRepository) : BaseViewModel() {
    var versionCode: String?
        get() = appRepository.versionCode
        set(value) {
            appRepository.versionCode = value
        }
}