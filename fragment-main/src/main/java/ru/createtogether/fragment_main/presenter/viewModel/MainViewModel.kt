package ru.createtogether.fragment_main.presenter.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.baseFragment.BaseViewModel
import ru.createtogether.feature_cache_impl.domain.PreferenceStorage
import ru.createtogether.fragment_holiday.R
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): BaseViewModel() {
    private var dateSelected: Date = Calendar.getInstance().time

    fun setDate(date: Date){
        this.dateSelected = date
    }

    fun getDate() = dateSelected

    fun setFavorite(isFavorite: Boolean) {
        snackBarResponse.value = Event.success(
            if (isFavorite)
                R.string.snack_add_to_favorite
            else
                R.string.snack_remove_from_favorite
        )
    }
}