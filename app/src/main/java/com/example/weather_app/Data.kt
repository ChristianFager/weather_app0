package com.example.weather_app


data class Data(val date: Long, val sunrise: Long, val sunset: Long, val temp: ForecastTemp,
val pressure: Float, val humidity: Int)