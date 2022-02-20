package ru.createtogether.feature_holiday_api.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.createtogether.feature_day_utils.model.DayModel
import ru.createtogether.feature_holiday_utils.model.HolidayByIdsRequest
import ru.createtogether.feature_holiday_utils.model.HolidayModel

interface HolidayApi {

    @GET("/holidays/{date}")
    suspend fun loadHolidays(@Path("date") date: String): Response<List<HolidayModel>>

    @GET("/holidays/{date}/nearest_holiday_day")
    suspend fun loadNextDayWithHolidays(@Path("date") date: String): Response<DayModel>

    @POST("/holidays")
    suspend fun loadHolidaysByIds(@Body holidayByIdsRequest: HolidayByIdsRequest): Response<List<HolidayModel>>

    @GET("/holidays/{date}/holidays_of_month_preview")
    suspend fun loadHolidaysOfMonth(@Path("date") date: String): Response<List<DayModel>>
}