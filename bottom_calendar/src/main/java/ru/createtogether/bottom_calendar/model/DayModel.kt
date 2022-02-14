package ru.createtogether.birthday.imageCalendar.model

import ru.createtogether.birthday.imageCalendar.helpers.DayStateEnum
import java.util.*

data class DayModel(
        var calendar: Calendar,
        var state: DayStateEnum,
        var isToday: Boolean
)