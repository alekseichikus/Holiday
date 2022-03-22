package ru.createtogether.fragment_about.presenter.viewModel

import androidx.lifecycle.ViewModel
import com.example.feature_app_impl.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.createtogether.common.helpers.baseFragment.BaseViewModel
import ru.createtogether.feature_cache_impl.domain.PreferenceStorage
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(private val appRepository: AppRepository): BaseViewModel() {
        var versionCode = appRepository.versionCode
}