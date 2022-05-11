package ru.createtogether.feature_day_utils.model

import java.util.*

data class MonthModel(
        var calendar: Calendar,
        val days: List<DayModel>
)