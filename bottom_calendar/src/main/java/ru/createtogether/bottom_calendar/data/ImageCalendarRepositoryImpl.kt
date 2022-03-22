package ru.createtogether.bottom_calendar.data

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.apache.commons.lang3.time.DateUtils
import ru.createtogether.bottom_calendar.helpers.CalendarConstants
import ru.createtogether.birthday.imageCalendar.helpers.DayStateEnum
import ru.createtogether.bottom_calendar.model.DayModel
import ru.createtogether.birthday.imageCalendar.model.MonthModel
import ru.createtogether.bottom_calendar.domain.ImageCalendarRepository

import ru.createtogether.common.helpers.Event
import ru.createtogether.feature_network_impl.domain.ErrorHandlerRepository
import java.util.*
import javax.inject.Inject

class ImageCalendarRepositoryImpl @Inject constructor() : ImageCalendarRepository {

    override fun loadCalendar(curCalendar: Calendar, startingAt: Int, now: Calendar) = flow {

        val months = mutableListOf<MonthModel>()
        (0 until CalendarConstants.MIN_MONTHS).forEach {
            with(curCalendar.clone() as Calendar) {
                add(Calendar.MONTH, it)
                months.add(
                    MonthModel(
                        this,
                        getDaysOfMonth(curCalendar = this, startingAt = startingAt, now = now)
                    )
                )
            }
        }
        emit(months)
    }

    private fun getDaysOfMonth(
        curCalendar: Calendar,
        startingAt: Int,
        now: Calendar
    ): List<DayModel> {
        val start = DateUtils.truncate(curCalendar, Calendar.DAY_OF_MONTH)
        if (start.get(Calendar.DAY_OF_WEEK) != (startingAt + 1)) {
            start.set(
                Calendar.DAY_OF_MONTH,
                if (isLessFirstWeek(curCalendar, startingAt = startingAt)) -startingAt else 0
            )
            start.add(
                Calendar.DAY_OF_MONTH,
                -start.get(Calendar.DAY_OF_WEEK) + 1 + startingAt
            )
        }

        val weekOfMonth =
            curCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + (if (isLessFirstWeek(
                    curCalendar, startingAt = startingAt
                )
            ) 1 else 0) - (if (isMoreLastWeek(curCalendar, startingAt = startingAt)) 1 else 0)

        return (0..(7 * weekOfMonth)).map {
            val cal = Calendar.getInstance().apply { time = start.time }
            cal.add(Calendar.DAY_OF_MONTH, it)

            val thisTime = curCalendar.get(Calendar.YEAR) * 12 + curCalendar.get(Calendar.MONTH)
            val compareTime = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH)

            val state = when (thisTime.compareTo(compareTime)) {
                -1 -> DayStateEnum.NEXT
                0 -> DayStateEnum.CURRENT
                1 -> DayStateEnum.PREVIOUS
                else -> throw IllegalStateException()
            }

            DayModel(
                cal,
                state,
                isToday = DateUtils.isSameDay(cal, now),
                count = 4
            )
        }
    }

    private fun isLessFirstWeek(calendar: Calendar, startingAt: Int): Boolean {
        return calendar.get(Calendar.DAY_OF_WEEK) < startingAt + 1
    }

    private fun isMoreLastWeek(calendar: Calendar, startingAt: Int): Boolean {
        val end = DateUtils.truncate(calendar, Calendar.DAY_OF_MONTH)
        end.add(Calendar.MONTH, 1)
        end.add(Calendar.DATE, -1)
        return end.get(Calendar.DAY_OF_WEEK) < startingAt + 1
    }
}