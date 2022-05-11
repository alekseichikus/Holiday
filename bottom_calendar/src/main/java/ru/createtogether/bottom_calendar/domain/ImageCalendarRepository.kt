package ru.createtogether.bottom_calendar.domain

import kotlinx.coroutines.flow.Flow
import ru.createtogether.feature_day_utils.model.MonthModel
import java.util.*


interface ImageCalendarRepository {
    fun loadCalendar(curCalendar: Calendar, startingAt: Int, now: Calendar): Flow<List<MonthModel>>
}