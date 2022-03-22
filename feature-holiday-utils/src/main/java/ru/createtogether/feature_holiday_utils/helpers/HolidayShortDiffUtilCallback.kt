package ru.createtogether.feature_holiday_utils.helpers

import androidx.recyclerview.widget.DiffUtil
import ru.createtogether.feature_holiday_utils.model.HolidayModel

class HolidayShortDiffUtilCallback(
    private var oldList: Array<HolidayModel>,
    private var newList: Array<HolidayModel>
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
        return oldProduct.id == newProduct.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]

        return (oldProduct.isLike == newProduct.isLike)
    }
}