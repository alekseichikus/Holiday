package ru.createtogether.feature_photo

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.createtogether.common.helpers.extension.gone
import ru.createtogether.common.helpers.extension.show
import ru.createtogether.feature_photo.databinding.ViewPhotoSmallBinding
import ru.createtogether.feature_photo_utils.PhotoModel

class PhotoSmallView(context: Context, var onPhotoClick: (PhotoModel) -> Unit) :
    FrameLayout(context) {

    private var _binding: ViewPhotoSmallBinding? = null
    private val binding get() = _binding!!

    private lateinit var photo: PhotoModel

    init {
        _binding = ViewPhotoSmallBinding.inflate(LayoutInflater.from(context), this, false)
        addView(binding.root)
        initView()
    }

    private fun initView() {
        initListeners()
    }

    private fun initListeners() {
        setPhotoClick()
    }

    private fun setSelectedStyle() {
        binding.llSelectedContainer.isVisible = photo.isSelected
    }

    private fun setPhotoClick() {
        binding.root.setOnClickListener {
            onPhotoClick(photo)
        }
    }

    fun setPhoto(photo: PhotoModel) {
        this.photo = photo
        loadPreview()
        setSelectedStyle()
    }

    private fun loadPreview() {
        Glide.with(context)
            .load(photo.url)
            .centerCrop()
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.ivPlaceholder.show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.ivPlaceholder.gone()
                    return false
                }

            })
            .transition(DrawableTransitionOptions.withCrossFade(Constants.ANIMATE_TRANSITION_DURATION))
            .into(binding.ivPhoto)
    }
}