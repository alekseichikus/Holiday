package ru.createtogether.birthday.imageCalendar.helpers

import androidx.recyclerview.widget.DiffUtil
import ru.createtogether.birthday.imageCalendar.model.DayModel
import ru.createtogether.birthday.imageCalendar.model.MonthModel

class MonthDiffUtilCallback(
    private var oldList: List<MonthModel>,
    private var newList: List<MonthModel>
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

        return (oldProduct.calendar == newProduct.calendar)
    }

    fun calculateDiff() = DiffUtil.calculateDiff(this, false)
}