package ru.createtogether.feature_holiday.helpers

import com.example.feature_adapter_generator.BaseAction
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_photo_utils.PhotoModel

interface HolidayShortAdapterListener:
    com.example.feature_adapter_generator.BaseAction<HolidayModel> {
    fun onLikeClick(holiday: HolidayModel)
    fun onLongClick(holiday: HolidayModel)
    fun onPhotoClick(holiday: HolidayModel, photo: PhotoModel)
}