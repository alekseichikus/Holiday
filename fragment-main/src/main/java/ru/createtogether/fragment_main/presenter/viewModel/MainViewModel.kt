package ru.createtogether.fragment_main.presenter.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.createtogether.feature_cache_impl.domain.PreferenceStorage
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    var preferenceStorage: PreferenceStorage
): ViewModel() {
    var currentDate: Date = Calendar.getInstance().time

    var versionCode: String?
        get() = preferenceStorage.versionCode
        set(value) {
            preferenceStorage.versionCode = value
        }
}