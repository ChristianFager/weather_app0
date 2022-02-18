package com.example.weather_app


data class DayForecast(val date: Long? = 1643403034138, val sunrise: Long, val sunset: Long, val temp: ForecastTemp,
                       val pressure: Float, val humidity: Int)
    /*val date: Long? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: ForecastTemp? = null,
    val pressure: Float? = null,
    val humidity: Int? = null,
)
*/