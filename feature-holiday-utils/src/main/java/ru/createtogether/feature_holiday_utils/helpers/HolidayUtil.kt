package ru.createtogether.feature_holiday_utils.helpers

import com.example.feature_adapter_generator.DiffUtilTheSameCallback
import ru.createtogether.common.helpers.extension.onOpen
import ru.createtogether.feature_holiday_utils.R
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_photo_utils.PhotoModel

object HolidayUtil {
    fun getTypeName(holidayTypeEnum: HolidayTypeEnum?): Int {
        return when (holidayTypeEnum) {
            HolidayTypeEnum.RELIGION -> R.string.category_type_religion
            HolidayTypeEnum.FAMILY -> R.string.category_type_family
            else -> R.string.category_type_another
        }
    }

    fun getHolidayBaseDiffUtilTheSameCallback() = object :
        DiffUtilTheSameCallback<HolidayModel> {
        override fun areItemsTheSame(oldItem: HolidayModel, newItem: HolidayModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: HolidayModel, newItem: HolidayModel) =
            oldItem.isLike == newItem.isLike
    }

    fun getHolidayShortAdapterListener(
        onLikeClick: ((HolidayModel) -> Unit)? = null,
        onLongClick: ((HolidayModel) -> Unit)? = null,
        onPhotoClick: ((HolidayModel, PhotoModel) -> Unit)? = null,
        onClick: ((HolidayModel) -> Unit)? = null
    ) = object : HolidayShortAdapterListener {
        override fun onLikeClick(holiday: HolidayModel) {
            onLikeClick?.invoke(holiday)
        }

        override fun onLongClick(holiday: HolidayModel) {
            onLongClick?.invoke(holiday)
        }

        override fun onPhotoClick(holiday: HolidayModel, photo: PhotoModel) {
            onPhotoClick?.invoke(holiday, photo)
        }

        override fun onClick(item: HolidayModel) {
            onClick?.invoke(item)
        }
    }
}