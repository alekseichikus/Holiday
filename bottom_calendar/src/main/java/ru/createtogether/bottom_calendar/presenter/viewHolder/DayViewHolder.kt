package ru.createtogether.birthday.imageCalendar.presenter.viewHolder

import android.content.res.ColorStateList
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.createtogether.birthday.imageCalendar.helpers.DayStateEnum
import ru.createtogether.bottom_calendar.model.DayModel
import ru.createtogether.bottom_calendar.R
import ru.createtogether.bottom_calendar.databinding.ItemDayBinding
import java.util.*


class DayViewHolder(
    private val binding: ItemDayBinding,
    private val onDayClick: (DayModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context

    private lateinit var day: DayModel
    fun bind(day: DayModel) {
        this.day = day
        setText()
        setCountHolidays()
        initListeners()
        setRootHeight()
        setSelected()
    }

    private fun initListeners() {
        setRootClick()
    }

    private fun setRootClick() {
        binding.root.setOnClickListener {
            onDayClick(day)
        }
    }

    private fun setSelected() {
        binding.tvDay.backgroundTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    if (day.isToday) R.color.textTertiary else R.color.backgroundContent
                )
            )
    }

    private fun setText() {
        with(binding.tvDay) {
            text = day.calendar.get(Calendar.DAY_OF_MONTH).toString()

            setTextColor(
                ContextCompat.getColor(
                    context,
                    if (day.isToday)
                        R.color.backgroundFill
                    else if (day.state == DayStateEnum.CURRENT && day.count > 0)
                        R.color.textPrimary
                    else if (day.state != DayStateEnum.CURRENT)
                        R.color.backgroundContent
                    else
                        R.color.textTertiary
                )
            )
        }
    }

    private fun setRootHeight() {
        binding.root.layoutParams =
            ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                context.resources.displayMetrics.widthPixels / 7
            )
    }

    private fun setCountHolidays() {
        binding.tvCountHolidays.isVisible = day.count > 1
        if (day.count > 1)
            binding.tvCountHolidays.text = day.count.toString()
    }
}