package ru.createtogether.fragment_holiday

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.createtogether.common.helpers.AdapterActions
import ru.createtogether.common.helpers.MainActions
import ru.createtogether.common.helpers.baseFragment.BaseBottomDialogFragment
import ru.createtogether.common.helpers.extension.*
import ru.createtogether.feature_characteristic.adapter.CharacteristicAdapter
import ru.createtogether.feature_characteristic_utils.CharacteristicModel
import ru.createtogether.feature_country_utils.helpers.CountryEnum
import ru.createtogether.feature_holiday_impl.viewModel.HolidayViewModel
import ru.createtogether.feature_holiday_utils.helpers.HolidayTypeEnum
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_photo.adapter.PhotoAdapter
import ru.createtogether.feature_photo_utils.PhotoModel
import ru.createtogether.fragment_holiday.databinding.FragmentHolidayBinding
import ru.createtogether.fragment_photo.presenter.PhotoFragment
import java.util.*

@AndroidEntryPoint
class HolidayFragment : BaseBottomDialogFragment(R.layout.fragment_holiday) {
    private val binding: FragmentHolidayBinding by viewBinding()
    private val holidayViewModel: HolidayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
        initListeners()

        setHoliday(holiday)
        with(holiday) {
            if (images.isNullOrEmpty()) {
                binding.ivPreview.gone()
                binding.progressBar.gone()
                binding.tvImageAbsent.show()
            } else
                binding.tvImageAbsent.gone()
        }
    }

    private fun configureViews() {
        binding.clButtonContainer.setPaddingTopMenu()
    }

    private fun initListeners() {
        setCloseClick()
        setLikeClick()
        setShareClick()
        setOffsetChanged()
        setPhotoClick()
    }

    private fun setPhotoClick(){
        binding.imageView.setOnClickListener {
            holiday.images?.let {
                onPhotoClick(it.first())
            }
        }
    }

    private fun setLikeClick() {
        binding.ivLike.setOnClickListener {
            with(holiday) {
                isLike = isLike.not()
                (requireActivity() as MainActions).showSnackBar(
                    if (isLike)
                        R.string.snack_add_to_favorite
                    else
                        R.string.snack_remove_from_favorite
                )
                if (isLike)
                    holidayViewModel.addHolidayLike(id)
                else
                    holidayViewModel.removeHolidayLike(id)
                setLike()
            }
        }
    }

    private fun setCloseClick() {
        binding.ivClose.setOnClickListener {
            onBack()
        }
    }

    private fun setShareClick() {
        binding.ivShare.setOnClickListener {
            shareText(StringBuilder().apply {
                appendLine(holiday.title)
                appendLine(
                    Calendar.getInstance()
                        .setDateString(holiday.date).time.withPattern(Constants.DEFAULT_DATE_PATTERN)
                )
                appendLine(holiday.description)
            })
        }
    }

    private fun shareText(text: StringBuilder) {
        startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtras(bundleOf(Intent.EXTRA_TEXT to text))
        }, getString(R.string.holiday)))
    }

    private fun setOffsetChanged() {
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            val appBarHeight = appBarLayout.measuredHeight
            val calcNumber = (appBarHeight + verticalOffset) / appBarHeight.toFloat()
            with(binding) {
                rvCharacteristic.updatePadding(top = ((1 - calcNumber) * getTopOffset()).toInt())

                ivClose.alpha = calcNumber
                ivShare.alpha = calcNumber
            }
        })
    }

    private fun getType(holidayTypeEnum: HolidayTypeEnum?): String {
        return when (holidayTypeEnum) {
            HolidayTypeEnum.RELIGION -> requireContext().getString(R.string.category_type_religion)
            HolidayTypeEnum.FAMILY -> requireContext().getString(R.string.category_type_family)
            else -> requireContext().getString(R.string.category_type_another)
        }
    }

    private fun initPhotoSmallAdapter(images: List<PhotoModel>) {
        with(binding.rvPhoto) {
            if (adapter == null)
                adapter =
                    PhotoAdapter(
                        images.toMutableList(),
                        ::onPhotoClick
                    )
            else
                (adapter as AdapterActions).setData(images)
        }
    }

    private fun setLike() {
        val icon =
            if (holiday.isLike) R.drawable.ic_bookmark else R.drawable.ic_bookmark_r
        binding.ivLike.setImageResource(icon)
    }

    private fun onPhotoClick(photo: PhotoModel) {
        holiday.images?.let {
            onOpen(
                PhotoFragment.getInstance(photos = it.toTypedArray(), position = it.indexOf(photo)),
                isAdd = true
            )
        }
    }

    private fun setPhotosAdapter(holidayResponse: HolidayModel) {
        holidayResponse.images?.let { initPhotoSmallAdapter(it) }
        binding.clPhotosContainer.isVisible = holidayResponse.images.isNullOrEmpty().not()
    }

    private fun setTitle() {
        binding.tvTitle.text = holiday.title
    }

    private fun setDescription() {
        with(binding.tvDescription) {
            isVisible = holiday.description.isNotEmpty()
            text = holiday.description
        }
    }

    private fun setCharacteristics() {
        var charDate: String
        with(Calendar.getInstance().setDateString(holiday.date).time){
            charDate = "${withPattern(Constants.PATTERN_D)} ${withPattern(Constants.PATTERN_MMMM)}"
        }

        val charType = getType(holidayTypeEnum = holiday.type)
        val charCountry = when (holiday.region) {
            CountryEnum.RU -> getString(R.string.country_russia)
            else -> getString(R.string.international)
        }

        initCharacteristicAdapter(
            listOf(
                CharacteristicModel(
                    imageRes = R.drawable.ic_quote_right,
                    text = getString(R.string.type),
                    description = charType
                ),
                CharacteristicModel(
                    imageRes = R.drawable.ic_calendar,
                    text = getString(R.string.date),
                    description = charDate
                ),
                CharacteristicModel(
                    imageRes = R.drawable.ic_globe,
                    text = getString(R.string.country),
                    description = charCountry
                ),
            )
        )
    }

    private fun setHoliday(holidayResponse: HolidayModel) {
        holidayResponse.images?.first()?.let { loadPreview(it) }

        setTitle()
        setLike()
        setDescription()
        setCharacteristics()

        setPhotosAdapter(holidayResponse = holidayResponse)
    }

    private fun initCharacteristicAdapter(list: List<CharacteristicModel>) {
        with(binding.rvCharacteristic) {
            if (adapter == null)
                adapter = CharacteristicAdapter(list.toMutableList())
        }
    }

    private fun loadPreview(image: PhotoModel) {
        Glide.with(requireContext())
            .load(image.url)
            .transition(DrawableTransitionOptions.withCrossFade(Constants.ANIMATE_TRANSITION_DURATION))
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.ivPreview.show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.ivPreview.gone()
                    binding.progressBar.gone()
                    return false
                }


            })
            .into(binding.imageView)
    }

    private val holiday by lazy {
        requireArguments().getSerializable(PARAM_HOLIDAY) as HolidayModel
    }

    companion object {
        private const val PARAM_HOLIDAY = "holiday"
        fun getInstance(holiday: HolidayModel) = HolidayFragment().apply {
            arguments = bundleOf(PARAM_HOLIDAY to holiday)
        }
    }
}