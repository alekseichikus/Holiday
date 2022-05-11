package ru.createtogether.bottom_calendar.helpers

import ru.createtogether.feature_day_utils.model.MonthModel
import ru.createtogether.feature_day_utils.model.DayModel
import com.example.feature_adapter_generator.BaseAction

interface MonthAdapterListener: BaseAction<MonthModel> {
    fun onDayClick(day: DayModel)
}