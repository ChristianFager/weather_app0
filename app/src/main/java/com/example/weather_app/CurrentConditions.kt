package com.example.weather_app


data class CurrentConditions
(
    val weather: List<WeatherCondition>,
    val main: Currents,
    val name: String,
)