package ru.createtogether.bottom_calendar.helpers

import ru.createtogether.birthday.imageCalendar.model.MonthModel
import ru.createtogether.bottom_calendar.model.DayModel
import com.example.feature_adapter_generator.BaseAction

interface MonthAdapterListener: com.example.feature_adapter_generator.BaseAction<MonthModel> {
    fun onDayClick(day: DayModel)
}