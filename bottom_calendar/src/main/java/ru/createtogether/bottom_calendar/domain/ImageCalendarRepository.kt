package ru.createtogether.bottom_calendar.domain

import kotlinx.coroutines.flow.Flow
import ru.createtogether.birthday.imageCalendar.model.MonthModel
import ru.createtogether.common.helpers.Event
import java.util.*


interface ImageCalendarRepository {
    fun loadCalendar(curCalendar: Calendar, startingAt: Int, now: Calendar): Flow<List<MonthModel>>
}