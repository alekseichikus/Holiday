package ru.createtogether.feature_photo.helpers

import androidx.recyclerview.widget.DiffUtil
import ru.createtogether.feature_photo_utils.PhotoModel

class PhotoDiffUtilCallback(
    private var oldList: List<PhotoModel>,
    private var newList: List<PhotoModel>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct.url == newProduct.url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]

        return (oldProduct.isSelected == newProduct.isSelected)
    }
}