package ru.createtogether.bottom_calendar.presenter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import ru.createtogether.bottom_calendar.model.DayModel
import ru.createtogether.birthday.imageCalendar.model.MonthModel
import ru.createtogether.bottom_calendar.adapter.DayAdapter
import ru.createtogether.bottom_calendar.databinding.ItemMonthBinding
import ru.createtogether.common.helpers.AdapterActions
import ru.createtogether.common.helpers.extension.withPattern


class MonthViewHolder(
    private val binding: ItemMonthBinding,
    private val onDayClick: (DayModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context
    private lateinit var month: MonthModel

    fun bind(month: MonthModel) {
        this.month = month
        setText()
        initDayAdapter()
    }

    private fun setText(){
        binding.tvMonth.text = month.calendar.time.withPattern(Constants.PATTERN_MMMM).replaceFirstChar(Char::titlecase)
    }

    private fun initDayAdapter() {
        with(binding.rvDays) {
            if (adapter == null)
                adapter = DayAdapter(month.days, onDayClick)
            else
                (adapter as AdapterActions).setData(month.days)
        }
    }
}