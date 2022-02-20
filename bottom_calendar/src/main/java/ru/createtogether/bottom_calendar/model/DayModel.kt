package ru.createtogether.bottom_calendar.model

import ru.createtogether.birthday.imageCalendar.helpers.DayStateEnum
import java.util.*

data class DayModel(
        var calendar: Calendar,
        var state: DayStateEnum,
        var isToday: Boolean,
        var count: Int
)