package ru.createtogether.feature_holiday

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.feature_adapter_generator.initAdapter
import ru.createtogether.common.helpers.extension.setDateString
import ru.createtogether.common.helpers.extension.withPattern
import ru.createtogether.feature_holiday.databinding.ViewHolidayShortBinding
import ru.createtogether.feature_holiday_utils.helpers.HolidayShortAdapterListener
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_photo.PhotoSmallView
import ru.createtogether.feature_photo_utils.PhotoModel
import java.util.*

class HolidayShortView(
    context: Context,
    var adapterListener: HolidayShortAdapterListener
) : FrameLayout(context), com.example.feature_adapter_generator.ViewAction<HolidayModel> {

    private var binding: ViewHolidayShortBinding =
        ViewHolidayShortBinding.inflate(LayoutInflater.from(context), this, false)

    private lateinit var holiday: HolidayModel

    init {
        addView(binding.root)
        initView()
    }

    private fun initView() {
        initListeners()
    }

    private fun initListeners() {
        setLikeClick()
        setOpenClick()
        setOpenLongClick()
    }

    private fun setLikeClick() {
        binding.ivLike.setOnClickListener {
            with(holiday) {
                isLike = !isLike
                configureLike(isLike = isLike)
                adapterListener.onLikeClick(holiday = holiday)
            }
        }
    }

    private fun setOpenClick() {
        binding.root.setOnClickListener {
            adapterListener.onClick(item = holiday)
        }
    }

    private fun setOpenLongClick() {
        binding.root.setOnLongClickListener {
            adapterListener.onLongClick(holiday = holiday)
            return@setOnLongClickListener true
        }
    }

    private fun setTitle(text: String) {
        binding.tvTitle.text = text
    }

    private fun setDescription(text: String) {
        binding.tvDescription.text = text
        binding.tvDescription.isVisible = text.isNotEmpty()
    }

    private fun setDate(date: String) {
        with(Calendar.getInstance().setDateString(date = date)) {
            binding.tvDate.text =
                "${time.withPattern(Constants.PATTERN_D)} ${time.withPattern(Constants.PATTERN_MMM)}"
        }
    }

    private fun configureLike(isLike: Boolean) {
        binding.ivLike.setImageResource(
            if (isLike)
                R.drawable.ic_bookmark
            else
                R.drawable.ic_bookmark_r
        )
    }

    private fun setType(holidayTypeEnum: ru.createtogether.feature_holiday_utils.helpers.HolidayTypeEnum?) {
        holidayTypeEnum?.let {
            binding.tvCategoryHoliday.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, it.resColor))
        }

        binding.tvCategoryHoliday.text = context.getString(
            when (holidayTypeEnum) {
                ru.createtogether.feature_holiday_utils.helpers.HolidayTypeEnum.RELIGION -> R.string.category_type_religion
                ru.createtogether.feature_holiday_utils.helpers.HolidayTypeEnum.FAMILY -> R.string.category_type_family
                else -> R.string.category_type_another
            }
        )
    }

    fun setHoliday(holiday: HolidayModel) {
        this.holiday = holiday
        with(holiday) {
            setType(holidayTypeEnum = type)
            setTitle(title)
            setDescription(description)
            configureLike(isLike)
            setPhotosAdapter(this)
            setDate(date)
        }
    }

    private fun setPhotosAdapter(holidayResponse: HolidayModel) {
        if (holidayResponse.images.isNotEmpty())
            initPhotoSmallAdapter(holidayResponse.images)
        binding.clPhotosContainer.isVisible = holidayResponse.images.isNullOrEmpty().not()
    }

    private fun initPhotoSmallAdapter(images: Collection<PhotoModel>) {
        with(binding) {
            rvPhoto.initAdapter(
                images,
                PhotoSmallView::class.java,
                object : com.example.feature_adapter_generator.BaseAction<PhotoModel> {
                    override fun onClick(item: PhotoModel) {
                        onPhotoClick(photo = item)
                    }
                },
                object :
                    com.example.feature_adapter_generator.DiffUtilTheSameCallback<PhotoModel> {
                    override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel) =
                        oldItem.id == newItem.id

                    override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel) =
                        oldItem.isSelected == newItem.isSelected
                }
            )
        }
    }

    private fun onPhotoClick(photo: PhotoModel) {
        adapterListener.onPhotoClick(holiday = holiday, photo = photo)
    }

    override fun initData(item: HolidayModel) {
        setHoliday(holiday = item)
    }
}