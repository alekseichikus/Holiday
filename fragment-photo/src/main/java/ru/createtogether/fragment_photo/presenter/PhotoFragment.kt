package ru.createtogether.fragment_photo.presenter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IntRange
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import dagger.hilt.android.AndroidEntryPoint
import ru.createtogether.common.helpers.AdapterActions
import ru.createtogether.common.helpers.MainActions
import ru.createtogether.common.helpers.Status
import ru.createtogether.common.helpers.baseFragment.BaseFragment
import ru.createtogether.common.helpers.extension.gone
import ru.createtogether.common.helpers.extension.onBack
import ru.createtogether.common.helpers.extension.setPaddingTopMenu
import ru.createtogether.common.helpers.extension.show
import ru.createtogether.feature_photo.adapter.PhotoAdapter
import ru.createtogether.feature_photo_utils.PhotoModel
import ru.createtogether.fragment_photo.R
import ru.createtogether.fragment_photo.customView.SwipeBackLayout
import ru.createtogether.fragment_photo.databinding.FragmentPhotoBinding
import ru.createtogether.fragment_photo.presenter.viewModel.PhotoViewModel
import kotlin.math.roundToInt


@AndroidEntryPoint
class PhotoFragment : BaseFragment(R.layout.fragment_photo) {
    private val binding: FragmentPhotoBinding by viewBinding()

    private val photoViewModel: PhotoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        configureViews()
        initListeners()
        initObservers()

        loadImage(photos[position])
    }

    private fun initObservers() {
        observePhotos()
    }

    private fun observePhotos() {
        photoViewModel.photos.observe(viewLifecycleOwner) {
            initPhotoSmallAdapter(images = it.map { it.copy() })
        }
    }

    private fun initData() {
        photoViewModel.photos.value = photos.toList()
    }

    private fun configureViews() {
        setPaddingContent()
        (requireActivity() as MainActions).changeNavigationBarColor(R.color.black)
        setBackgroundAlpha(FULL_TRANSPARENCY)
    }

    private fun setBackgroundAlpha(@IntRange(from = 0, to = FULL_TRANSPARENCY.toLong()) alpha: Int) {
        binding.root.background.alpha = alpha
    }

    private fun setPaddingContent() {
        binding.clContainer.setPaddingTopMenu()
    }

    private fun initPhotoSmallAdapter(images: List<PhotoModel>) {
        with(binding.rvPhoto) {
            if (adapter == null)
                adapter = PhotoAdapter(images.toMutableList(), ::onPhotoClick)
            else
                (adapter as AdapterActions).setData(images)
        }
    }

    private fun onPhotoClick(photo: PhotoModel) {
        with(photoViewModel.photos){
            value?.forEach { photo_ ->
                if(photo_.isSelected)
                    photo_.isSelected = false
                if(photo_.id == photo.id)
                    photo_.isSelected = true
            }
            postValue(value)
        }
        loadImage(photo)
    }

    private fun loadImage(image: PhotoModel) {
        Glide.with(requireContext())
            .load(image.url)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    setState(Status.ERROR)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    setState(Status.SUCCESS)
                    return false
                }


            })
            .into(binding.photoView)
    }

    private fun setState(status: Status) {
        when (status) {
            Status.ERROR -> {
                binding.progressBar.gone()
                binding.llErrorContainer.show()
            }
            Status.SUCCESS -> {
                binding.progressBar.show()
                binding.llErrorContainer.gone()
            }
            else -> {}
        }
    }

    private fun initListeners() {
        setSwipeBack()
        setBackPressed()
    }

    private fun setBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        })
    }

    private fun setSwipeBack() {
        binding.swipeBackLayout.setSwipeBackListener(object : SwipeBackLayout.OnSwipeBackListener {
            override fun onViewPositionChanged(
                mView: View?,
                swipeBackFraction: Float,
                swipeBackFactor: Float
            ) {
                with((FULL_TRANSPARENCY * (1 - swipeBackFraction)).roundToInt()) {
                    setBackgroundAlpha(
                        if (this > FULL_TRANSPARENCY)
                            FULL_TRANSPARENCY
                        else
                            this
                    )
                }
            }

            override fun onViewSwipeFinished(mView: View?, isEnd: Boolean) {
                if (isEnd)
                    onBack()
            }
        })
    }

    fun onRefreshClick() {
        photoViewModel.photos.value?.let {
            it.find { photoModel -> photoModel.isSelected }?.let { photoModel ->
                loadImage(photoModel)
            }
        }
    }

    fun onCloseClick(){
        onBack()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActions).changeNavigationBarColor(R.color.white)
    }

    private val photos by lazy {
        requireArguments().getSerializable(PARAM_PHOTOS) as Array<PhotoModel>
    }

    private val position by lazy {
        requireArguments().getInt(PARAM_POSITION)
    }

    companion object {
        private const val PARAM_PHOTOS = "photos"
        private const val PARAM_POSITION = "position"
        private const val FULL_TRANSPARENCY = 0xFF
        fun getInstance(photos: Array<PhotoModel>, position: Int): PhotoFragment {
            return PhotoFragment().apply {
                arguments = bundleOf(PARAM_PHOTOS to photos, PARAM_POSITION to position)
            }
        }
    }
}