package ru.createtogether.feature_holiday.helpers

import com.example.feature_adapter_generator.BaseAction
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_photo_utils.PhotoModel

interface HolidayShortListener: com.example.feature_adapter_generator.BaseAction<HolidayModel> {
    fun onHolidayPhotoClick(holiday: HolidayModel, photo: PhotoModel)
    fun onHolidayLongClick(holiday: HolidayModel)
    fun onChangeFavorite()
}