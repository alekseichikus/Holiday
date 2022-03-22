package ru.createtogether.bottom_calendar.customView

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.feature_adapter_generator.initAdapter
import ru.createtogether.birthday.imageCalendar.model.MonthModel
import ru.createtogether.bottom_calendar.databinding.ItemMonthBinding
import ru.createtogether.bottom_calendar.helpers.MonthAdapterListener
import ru.createtogether.bottom_calendar.model.DayModel
import ru.createtogether.common.helpers.extension.withPattern

class MonthView constructor(context: Context, var adapterListener: MonthAdapterListener) :
    LinearLayout(context), com.example.feature_adapter_generator.ViewAction<MonthModel> {

    private val binding = ItemMonthBinding.inflate(LayoutInflater.from(context), this, false)

    private lateinit var month: MonthModel

    init {
        addView(binding.root)
    }

    private fun setText() {
        binding.tvMonth.text = month.calendar.time.withPattern(Constants.PATTERN_MMMM)
            .replaceFirstChar(Char::titlecase)
    }

    private fun initDayAdapter() {
        binding.rvDays.initAdapter(
            month.days,
            DayView::class.java,
            object : com.example.feature_adapter_generator.BaseAction<DayModel> {
                override fun onClick(item: DayModel) {
                    adapterListener.onDayClick(day = item)
                }
            },
            object : com.example.feature_adapter_generator.DiffUtilTheSameCallback<DayModel> {
                override fun areItemsTheSame(oldItem: DayModel, newItem: DayModel) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: DayModel, newItem: DayModel) =
                    oldItem.calendar.compareTo(oldItem.calendar) == 0
            }
        )
    }

    fun setMonth(month: MonthModel) {
        this.month = month
        setText()
        initDayAdapter()
    }

    override fun initData(item: MonthModel) {
        setMonth(month = item)
    }
}