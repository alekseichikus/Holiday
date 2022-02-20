package ru.createtogether.feature_holiday

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import ru.createtogether.common.helpers.extension.setDateString
import ru.createtogether.common.helpers.extension.withPattern
import ru.createtogether.feature_holiday.databinding.ViewHolidayBinding
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_photo_utils.PhotoModel
import java.util.*

class HolidayView(context: Context, var attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var _binding: ViewHolidayBinding? = null
    private val binding get() = _binding!!

    lateinit var holiday: HolidayModel
    private lateinit var onLikeClick: ((HolidayModel) -> Unit?)

    init {
        _binding = ViewHolidayBinding.inflate(LayoutInflater.from(context), this, false)
        addView(binding.root)
        initView()
    }

    private fun initView() {
        initListeners()
    }

    private fun initListeners() {
        setLikeClick()
    }

    private fun setLikeClick() {
        binding.ivLike.setOnClickListener {
            with(holiday) {
                isLike = !isLike
                configureLike(isLike = isLike)
                onLikeClick(holiday)
            }
        }
    }

    private fun setTitle(text: String) {
        binding.tvTitle.text = text
    }

    private fun setDescription(text: String) {
        binding.tvDescription.text = text
    }

    private fun setDay(text: String) {
        binding.tvHolidayDay.text = text
    }

    private fun setMonth(text: String) {
        binding.tvHolidayMonth.text = text
    }

    private fun setImages(images: List<PhotoModel>?) {

        Glide.with(context)
            .load(images?.first()?.url)
            .into(binding.imageView)
    }

    private fun setDate(date: String) {
        with(Calendar.getInstance().setDateString(date = date)) {
            setDay(time.withPattern(Constants.PATTERN_D))
            setMonth(time.withPattern(Constants.PATTERN_MMM))
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

    fun initHoliday(holiday: HolidayModel) {
        this.holiday = holiday
        with(holiday) {
            setTitle(title)
            setDescription(description)
            setDate(date)
            configureLike(isLike)
            setType(type)
            setImages(images = images)
        }
    }

    fun setLikeClickListener(onLikeClick: (HolidayModel) -> Unit?) {
        this.onLikeClick = onLikeClick
    }
}