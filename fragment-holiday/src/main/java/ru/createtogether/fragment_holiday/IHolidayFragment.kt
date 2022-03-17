package ru.createtogether.fragment_holiday

import ru.createtogether.feature_characteristic_utils.CharacteristicModel
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_photo_utils.PhotoModel

interface IHolidayFragment {

    fun initBindingData()

    fun configureViews()
    fun setPaddingViews()

    fun initListeners()
    fun onCloseClick()
    fun onLikeClick()
    fun onShareClick()
    fun setOffsetChanged()

    fun setPhotosAdapter()

    fun initCharacteristicAdapter(characteristics: Array<CharacteristicModel>)
    fun initPhotoSmallAdapter(images: Array<PhotoModel>)

    fun loadPhoto()
    fun getCharacteristics(): Array<CharacteristicModel>
    fun setHoliday()

    fun getHolidayParam(): HolidayModel
}