package ru.createtogether.feature_photo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import ru.createtogether.feature_photo.databinding.ViewLoadPhotoBinding
import ru.createtogether.feature_photo.helpers.PhotoAdapterListener
import ru.createtogether.feature_photo.helpers.PhotoLoadListener

class PhotoLoadView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var binding: ViewLoadPhotoBinding =
        ViewLoadPhotoBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)
    }

    fun updateState(result: Result<Boolean>){
        with(binding){
            mbAgain.isVisible = result.isSuccess.not()
            flShimmer.hideShimmer()
        }
    }

    fun setListeners(adapterListener: PhotoLoadListener){
        binding.adapterListener = adapterListener
    }

    fun getImageView() = binding.imageView
}