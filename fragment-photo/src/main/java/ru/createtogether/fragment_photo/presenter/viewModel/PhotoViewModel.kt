package ru.createtogether.fragment_photo.presenter.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.createtogether.common.helpers.baseFragment.BaseViewModel
import ru.createtogether.feature_photo_utils.PhotoModel
import ru.createtogether.feature_photo_utils.helpers.PhotoConstants
import ru.createtogether.fragment_photo.presenter.PhotoFragment
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.math.roundToInt

class PhotoViewModel : BaseViewModel() {
    val photos = MutableLiveData<List<PhotoModel>>()

    fun getStateTransparent(swipeBackFraction: Float): Int {
        with((PhotoConstants.FULLY_VISIBLE * (1 - swipeBackFraction)).roundToInt()) {
            return if (this > PhotoConstants.FULLY_VISIBLE)
                PhotoConstants.FULLY_VISIBLE
            else
                this
        }
    }

    fun setSelectedPhoto(photo: PhotoModel) {
        with(photos) {
            value.orEmpty().find { it.isSelected }?.isSelected = false
            photo.isSelected = true
            postValue(value)
        }
    }

    fun getSelectedPhoto(): PhotoModel =
        photos.value.orEmpty().find { it.isSelected } ?: throw IllegalArgumentException()
}