package com.example.weather_app
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Api
{
    @GET("weather")
    suspend fun getCurrentConditions
    (
        @Query("zip") zip: String = "98178",
        @Query("units") units: String = "imperial",
        @Query("appId") appId: String = "1a8e680481c1a649e85fafc805abee10",
    ) : CurrentConditions


    @GET("forecast/daily")
    suspend fun getForecast
    (
        @Query("zip") zip: String = "98178",
        @Query("cnt") cnt: String = "16",
        @Query("units") units: String = "imperial",
        @Query("appId") appId: String = "1a8e680481c1a649e85fafc805abee10",
    ) : Forecast
}