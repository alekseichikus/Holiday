package ru.createtogether.fragment_photo.presenter.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.createtogether.feature_photo_utils.PhotoModel
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(): ViewModel() {
    val photos = MutableLiveData<List<PhotoModel>>()
}