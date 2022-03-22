package ru.createtogether.feature_photo_utils.helpers

import com.example.feature_adapter_generator.BaseAction
import com.example.feature_adapter_generator.DiffUtilTheSameCallback
import ru.createtogether.feature_photo_utils.PhotoModel

object PhotoUtil {
    fun getDiffUtilTheSameCallback() = object : DiffUtilTheSameCallback<PhotoModel> {
        override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel) =
            oldItem.isSelected == newItem.isSelected
    }

    fun getPhotoAdapterListener(onClick: ((PhotoModel) -> Unit)? = null) = object : BaseAction<PhotoModel> {
        override fun onClick(item: PhotoModel) {
            onClick?.invoke(item)
        }
    }
}