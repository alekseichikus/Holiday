package ru.createtogether.birthday.imageCalendar.model

import ru.createtogether.birthday.imageCalendar.helpers.DayStateEnum
import java.util.*

data class MonthModel(
        var calendar: Calendar,
        val days: List<DayModel>
)