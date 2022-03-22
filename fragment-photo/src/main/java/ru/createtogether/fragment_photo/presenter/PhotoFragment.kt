package ru.createtogether.fragment_photo.presenter

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IntRange
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.feature_adapter_generator.BaseAction
import com.example.feature_adapter_generator.initAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.MainActions
import ru.createtogether.common.helpers.baseFragment.BaseFragment
import ru.createtogether.common.helpers.extension.*
import ru.createtogether.feature_photo.PhotoSmallView
import ru.createtogether.feature_photo_utils.PhotoModel
import ru.createtogether.feature_photo_utils.helpers.PhotoConstants
import ru.createtogether.fragment_photo.R
import ru.createtogether.fragment_photo.customView.SwipeBackLayout
import ru.createtogether.fragment_photo.databinding.FragmentPhotoBinding
import ru.createtogether.feature_photo_utils.helpers.PhotoUtil
import ru.createtogether.fragment_photo.presenter.viewModel.PhotoViewModel

@AndroidEntryPoint
class PhotoFragment : BaseFragment(R.layout.fragment_photo), IPhotoFragment {
    companion object {
        private const val PARAM_PHOTOS = "photos"
        private const val PARAM_POSITION = "position"

        fun getInstance(photos: List<PhotoModel>, position: Int) = PhotoFragment().apply {
            arguments = bundleOf(PARAM_PHOTOS to photos.toTypedArray(), PARAM_POSITION to position)
        }
    }

    override val viewModel: PhotoViewModel by viewModels()

    private val binding: FragmentPhotoBinding by viewBinding()

    private val photos by lazy {
        requireArguments().getSerializable(PARAM_PHOTOS) as Array<PhotoModel>
    }

    private val position by lazy {
        requireArguments().getInt(PARAM_POSITION)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataBinding()
        initViewModelData()
        configureViews()
        initListeners()
        initObservers()

        loadImage(photos[position])
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActions).changeNavigationBarColor(R.color.white)
    }

    private fun initDataBinding(){
        binding.photoFragment = this
    }

    private fun initViewModelData() {
        viewModel.photos.value = photos
        viewModel.setSelectedPhoto(photo = photos[position])
    }

    private fun configureViews() {
        setPaddingContent()
        (requireActivity() as MainActions).changeNavigationBarColor(R.color.black)
        setBackgroundAlpha(PhotoConstants.FULLY_VISIBLE)
    }

    private fun setPaddingContent() {
        binding.clContainer.setPaddingTop()
    }

    private fun setBackgroundAlpha(
        @IntRange(
            from = 0,
            to = PhotoConstants.FULLY_VISIBLE.toLong()
        ) alpha: Int
    ) {
        binding.root.background.alpha = alpha
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
                mView: View,
                swipeBackFraction: Float,
                swipeBackFactor: Float
            ) {
                setBackgroundAlpha(viewModel.getStateTransparent(swipeBackFraction = swipeBackFraction))
            }

            override fun onViewSwipeFinished(mView: View, isEnd: Boolean) {
                if (isEnd)
                    onBack()
            }
        })
    }

    override fun onRefreshClick() {
        loadImage(viewModel.getSelectedPhoto())
    }

    override fun onCloseClick() {
        onBack()
    }

    private fun initObservers() {
        observePhotos()
    }

    private fun observePhotos() {
        viewModel.photos.observe(viewLifecycleOwner) { images ->
            initPhotoSmallAdapter(images = images.toList())
        }
    }

    private fun initPhotoSmallAdapter(images: List<PhotoModel>) {
        binding.rvPhoto.initAdapter(
            images,
            PhotoSmallView::class.java,
            PhotoUtil.getPhotoAdapterListener(onClick = { photo ->
                viewModel.setSelectedPhoto(photo = photo)
                loadImage(photo)
            }),
            PhotoUtil.getDiffUtilTheSameCallback()
        )
    }

    private fun loadImage(image: PhotoModel) {
        binding.photoView.loadImage(lifecycleScope, image.url, onSuccess = {
            binding.progressBar.show()
            binding.llErrorContainer.gone()
        }, onError = {
            binding.progressBar.gone()
            binding.llErrorContainer.show()
        })
    }
}