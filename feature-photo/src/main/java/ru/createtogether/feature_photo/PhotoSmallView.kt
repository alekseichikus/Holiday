package ru.createtogether.feature_photo

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.example.feature_adapter_generator.BaseAction
import com.example.feature_adapter_generator.ViewAction
import ru.createtogether.common.helpers.extension.gone
import ru.createtogether.common.helpers.extension.loadImage
import ru.createtogether.common.helpers.extension.show
import ru.createtogether.feature_photo.databinding.ViewPhotoSmallBinding
import ru.createtogether.feature_photo_utils.PhotoModel

class PhotoSmallView constructor(context: Context) :
    FrameLayout(context), ViewAction<PhotoModel> {

    private val binding = ViewPhotoSmallBinding.inflate(LayoutInflater.from(context), this, false)

    private lateinit var photo: PhotoModel

    private var baseAction: BaseAction<PhotoModel>? = null

    init {
        addView(binding.root)
        initListeners()
    }

    private fun initListeners() {
        setPhotoClick()
    }

    private fun setSelectedStyle(isSelected: Boolean) {
        binding.llSelectedContainer.isVisible = isSelected
    }

    private fun setPhotoClick() {
        binding.root.setOnClickListener {
            val newPhoto = photo.copy(isSelected = isSelected.not())
            setSelectedStyle(newPhoto.isSelected)

            baseAction?.onClick(item = newPhoto)
        }
    }

    private fun setPhoto(photo: PhotoModel) {
        this.photo = photo
        loadImage()
        setSelectedStyle(photo.isSelected)
    }

    private fun loadImage() {
        binding.ivPhoto.loadImage(photo.url, onSuccess = {
            binding.ivPlaceholder.gone()
        }, onError = {
            binding.ivPlaceholder.show()
        })
    }

    override fun initData(item: PhotoModel, baseAction: BaseAction<PhotoModel>?) {
        setPhoto(photo = item)
        this.baseAction = baseAction
    }
}