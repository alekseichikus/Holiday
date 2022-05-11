package ru.createtogether.feature_day_utils.model

import ru.createtogether.feature_day_utils.model.helpers.DayStateEnum
import java.util.*

data class DayModel(
        var calendar: Calendar,
        var state: DayStateEnum,
        var isToday: Boolean,
        var count: Int
)