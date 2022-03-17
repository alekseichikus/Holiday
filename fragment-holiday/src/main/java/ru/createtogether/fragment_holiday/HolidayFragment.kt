package ru.createtogether.fragment_holiday

import Constants
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.createtogether.common.helpers.baseFragment.BaseFragment
import ru.createtogether.common.helpers.extension.*
import ru.createtogether.feature_characteristic.adapter.CharacteristicAdapter
import ru.createtogether.feature_characteristic_utils.CharacteristicModel
import ru.createtogether.feature_country_utils.helpers.CountryUtil
import ru.createtogether.feature_holiday_impl.viewModel.BaseHolidayViewModel
import ru.createtogether.feature_holiday_utils.helpers.HolidayUtil
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_photo.adapter.PhotoAdapter
import ru.createtogether.feature_photo.helpers.PhotoAdapterListener
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

        setHoliday()
    }

    override fun initBindingData() {
        binding.holiday = getHolidayParam()
        binding.holidayFragment = this
    }

    override fun configureViews() {
        setPaddingViews()
        initPhotoLoadView()
    }

    override fun setPaddingViews() {
        binding.mlTopMenuContainer.setPaddingTop()
    }

    override fun initListeners() {
        setOffsetChanged()
    }

    private fun initPhotoLoadView() {
        binding.viewPhotoLoad.setListeners(object : PhotoLoadListener {
            override fun onClick() {
                onPhotoClick(getHolidayParam().images.first())
            }

            override fun onAgainClick() {
                loadPhoto()
            }
        })
    }

    override fun onLikeClick() {
        with(getHolidayParam()) {
            holidayViewModel.setFavorite(holiday = getHolidayParam())
            viewModel.setFavorite(isLike)

            binding.invalidateAll()
        }
    }

    private fun onPhotoClick(photo: PhotoModel) {
        onOpen(
            PhotoFragment.getInstance(
                photos = getHolidayParam().images.toTypedArray(),
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
        initPhotoSmallAdapter(getHolidayParam().images.toTypedArray())
    }

    override fun getCharacteristics() = arrayOf(
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

    override fun setHoliday() {
        loadPhoto()
        binding.ivLike.invalidate()
        initCharacteristicAdapter(getCharacteristics())

        setPhotosAdapter()
    }

    override fun loadPhoto() {
        binding.viewPhotoLoad.loadImage(lifecycleScope, getHolidayParam().images.first().url)
    }

    override fun initCharacteristicAdapter(characteristics: Array<CharacteristicModel>) {
        binding.rvCharacteristic.initAdapter(
            characteristics,
            CharacteristicAdapter::class.java
        )
    }

    override fun initPhotoSmallAdapter(images: Array<PhotoModel>) {
        binding.rvPhoto.initAdapter(
            images,
            PhotoAdapter::class.java,
            object : PhotoAdapterListener {
                override fun onClick(item: PhotoModel) {
                    onPhotoClick(item)
                }
            })
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
