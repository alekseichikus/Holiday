package ru.createtogether.bottom_calendar.customView

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.feature_adapter_generator.BaseAction
import com.example.feature_adapter_generator.ViewAction
import ru.createtogether.feature_day_utils.model.helpers.DayStateEnum
import ru.createtogether.bottom_calendar.R
import ru.createtogether.bottom_calendar.databinding.ItemDayBinding
import ru.createtogether.feature_day_utils.model.DayModel
import java.util.*

class DayView constructor(context: Context) :
    ConstraintLayout(context), ViewAction<DayModel> {

    private val binding = ItemDayBinding.inflate(LayoutInflater.from(context), this, false)

    private lateinit var day: DayModel

    private var adapterListener: BaseAction<DayModel>? = null

    init {
        addView(binding.root)
    }

    private fun initListeners() {
        setRootClick()
    }

    private fun setRootClick() {
        binding.root.setOnClickListener {
            adapterListener?.onClick(item = day)
        }
    }

    private fun setSelected() {
        binding.tvDay.backgroundTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    if (day.isToday) R.color.textLink else R.color.backgroundContent
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
            LayoutParams(context.resources.displayMetrics.widthPixels / 7,
                context.resources.displayMetrics.widthPixels / 7
            )
    }

    private fun setCountHolidays() {
        binding.tvCountHolidays.isVisible = day.count > 1
        if (day.count > 1)
            binding.tvCountHolidays.text = day.count.toString()
    }

    fun setDay(day: DayModel) {
        this.day = day
        setText()
        setCountHolidays()
        initListeners()
        setRootHeight()
        setSelected()
    }

    override fun initData(item: DayModel, baseAction: BaseAction<DayModel>?) {
        setDay(day = item)
    }
}