package ru.createtogether.birthday.imageCalendar.model

import ru.createtogether.bottom_calendar.model.DayModel
import java.util.*

data class MonthModel(
        var calendar: Calendar,
        val days: List<DayModel>
)