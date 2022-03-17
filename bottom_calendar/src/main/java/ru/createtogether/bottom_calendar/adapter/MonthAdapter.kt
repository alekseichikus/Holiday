package ru.createtogether.bottom_calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.createtogether.birthday.imageCalendar.helpers.MonthDiffUtilCallback
import ru.createtogether.bottom_calendar.model.DayModel
import ru.createtogether.birthday.imageCalendar.model.MonthModel
import ru.createtogether.bottom_calendar.presenter.viewHolder.MonthViewHolder
import ru.createtogether.bottom_calendar.databinding.ItemMonthBinding
import ru.createtogether.common.helpers.AdapterActions
import ru.createtogether.common.helpers.baseFragment.base.adapter.BaseAction
import kotlin.properties.Delegates

class MonthAdapter(private val initMonths: List<MonthModel>, private val onDayClick: (DayModel) -> Unit) :
    RecyclerView.Adapter<MonthViewHolder>(),
    AdapterActions<MonthModel> {

    var months: List<MonthModel> by Delegates.observable(emptyList()) { _, old, new ->
        MonthDiffUtilCallback(old, new).calculateDiff().dispatchUpdatesTo(this)
    }

    init {
        months = initMonths
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        return MonthViewHolder(
            ItemMonthBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onDayClick
        )
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bind(months[position])
    }

    override fun getItemCount() = months.size
    override fun getData() = items

    override var items: Array<MonthModel> = emptyArray()
    override lateinit var action: BaseAction<MonthModel>

    override fun setData(array: Array<MonthModel>) {
        items = array
    }
}