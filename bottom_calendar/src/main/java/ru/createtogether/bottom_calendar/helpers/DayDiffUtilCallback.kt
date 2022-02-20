package ru.createtogether.birthday.imageCalendar.helpers

import androidx.recyclerview.widget.DiffUtil
import ru.createtogether.bottom_calendar.model.DayModel

class DayDiffUtilCallback(
    private var oldList: List<DayModel>,
    private var newList: List<DayModel>
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
        return oldProduct.calendar == newProduct.calendar
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]

        return (oldProduct == newProduct)
    }

    fun calculateDiff() = DiffUtil.calculateDiff(this, false)
}