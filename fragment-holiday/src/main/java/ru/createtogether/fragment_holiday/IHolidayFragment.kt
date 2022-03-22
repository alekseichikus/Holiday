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
    fun initPhotoSmallAdapter(images: List<PhotoModel>)

    fun setCharacteristicAdapter()
    fun initCharacteristicAdapter(characteristics: List<CharacteristicModel>)

    fun loadPhoto()
    fun getCharacteristics(): List<CharacteristicModel>

    fun getHolidayParam(): HolidayModel
}