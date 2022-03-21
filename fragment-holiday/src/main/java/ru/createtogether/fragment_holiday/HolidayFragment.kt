package ru.createtogether.fragment_holiday

import Constants
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.feature_adapter_generator.DiffUtilTheSameCallback
import com.example.feature_adapter_generator.initAdapter
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.createtogether.common.helpers.baseFragment.BaseFragment
import ru.createtogether.common.helpers.extension.*
import ru.createtogether.feature_characteristic.presenter.CharacteristicView
import ru.createtogether.feature_characteristic_utils.CharacteristicModel
import ru.createtogether.feature_country_utils.helpers.CountryUtil
import ru.createtogether.feature_holiday_impl.viewModel.BaseHolidayViewModel
import ru.createtogether.feature_holiday_utils.helpers.HolidayUtil
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_photo.PhotoSmallView
import ru.createtogether.feature_photo.helpers.PhotoLoadListener
import ru.createtogether.feature_photo.helpers.loadImage
import ru.createtogether.feature_photo_utils.PhotoModel
import ru.createtogether.fragment_holiday.databinding.FragmentHolidayBinding
import ru.createtogether.fragment_holiday.viewModel.HolidayViewModel
import ru.createtogether.fragment_photo.presenter.PhotoFragment


@AndroidEntryPoint
class HolidayFragment : BaseFragment(R.layout.fragment_holiday), IHolidayFragment {
    private val binding: FragmentHolidayBinding by viewBinding()
    private val holidayViewModel: BaseHolidayViewModel by viewModels()
    override val viewModel: HolidayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindingData()

        configureViews()
        initListeners()

        loadPhoto()
        setCharacteristicAdapter()
        setPhotosAdapter()
    }

    override fun initBindingData() {
        binding.holiday = getHolidayParam()
        binding.holidayFragment = this
    }

    override fun configureViews() {
        setPaddingViews()
    }

    override fun setPaddingViews() {
        binding.mlTopMenuContainer.setPaddingTop()
    }

    override fun initListeners() {
        setOffsetChanged()

        setPhotoLoadViewListener()
    }

    private fun setPhotoLoadViewListener() {
        binding.viewPhotoLoad.setListeners(object : PhotoLoadListener {
            override fun onAgainClick() {
                loadPhoto()
            }

            override fun onClick() {
                onPhotoClick(getHolidayParam().images.first())
            }
        })
    }

    override fun onLikeClick() {
        with(getHolidayParam()) {
            isLike = isLike.not()
            holidayViewModel.setFavorite(holiday = getHolidayParam())
            viewModel.setFavorite(isLike)

            binding.invalidateAll()
        }
    }

    private fun onPhotoClick(photo: PhotoModel) {
        onOpen(
            PhotoFragment.getInstance(
                photos = getHolidayParam().images,
                position = getHolidayParam().images.indexOf(photo)
            ),
            isAdd = true
        )
    }

    override fun onCloseClick() {
        onBack()
    }

    override fun onShareClick() {
        requireContext().shareText(
            getString(R.string.holiday),
            viewModel.getShareText(getHolidayParam())
        )
    }

    override fun setOffsetChanged() {
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            with(
                viewModel.getCoordinatorScrollRatio(
                    appBarHeight = appBarLayout.measuredHeight,
                    verticalOffset = verticalOffset
                )
            ) {
                binding.llNestedContainer.updatePadding(top = ((this) * getTopOffset()).toInt())

                binding.mlTopMenuContainer.progress = this
                binding.mlViewPhotoLoad.progress = this
            }
        })
    }

    override fun setPhotosAdapter() {
        binding.clPhotosContainer.isVisible = getHolidayParam().images.isNotEmpty()
        initPhotoSmallAdapter(getHolidayParam().images)
    }

    override fun setCharacteristicAdapter() {
        initCharacteristicAdapter(getCharacteristics())
    }

    override fun initCharacteristicAdapter(characteristics: List<CharacteristicModel>) {
        binding.rvCharacteristic.initAdapter(
            characteristics,
            CharacteristicView::class.java,
            diffUtil = object :
                DiffUtilTheSameCallback<CharacteristicModel> {
                override fun areItemsTheSame(oldItem: CharacteristicModel, newItem: CharacteristicModel) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: CharacteristicModel, newItem: CharacteristicModel) =
                    oldItem == newItem
            })
    }

    override fun initPhotoSmallAdapter(images: List<PhotoModel>) {
        binding.rvPhoto.initAdapter(
            images,
            PhotoSmallView::class.java,
            object : com.example.feature_adapter_generator.BaseAction<PhotoModel> {
                override fun onClick(item: PhotoModel) {
                    onPhotoClick(item)
                }
            },
            object : DiffUtilTheSameCallback<PhotoModel> {
                override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel) =
                    oldItem.isSelected == newItem.isSelected
            }
        )
    }

    override fun getCharacteristics() = listOf(
        CharacteristicModel(
            imageRes = R.drawable.ic_quote_right,
            text = getString(R.string.type),
            description = getString(HolidayUtil.getTypeName(holidayTypeEnum = getHolidayParam().type))
        ),
        CharacteristicModel(
            imageRes = R.drawable.ic_calendar,
            text = getString(R.string.date),
            description = getDateString(
                date = getHolidayParam().date,
                patterns = Constants.PATTERN_D_MMMM
            )
        ),
        CharacteristicModel(
            imageRes = R.drawable.ic_globe,
            text = getString(R.string.country),
            description = getString(CountryUtil.getCountryName(country = getHolidayParam().region))
        ),
    )

    override fun loadPhoto() {
        binding.viewPhotoLoad.loadImage(lifecycleScope, getHolidayParam().images.first().url)
    }

    override fun getHolidayParam() =
        requireArguments().getSerializable(PARAM_HOLIDAY) as HolidayModel

    companion object {
        private const val PARAM_HOLIDAY = "holiday"
        fun getInstance(holiday: HolidayModel) = HolidayFragment().apply {
            arguments = bundleOf(PARAM_HOLIDAY to holiday)
        }
    }
}
