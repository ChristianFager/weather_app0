package com.example.weather_app


class ForecastTemp(tempDay: Float, tempMin: Float, tempMax: Float)
{
    private var day: Float = tempDay
    private var min: Float = tempMin
    private var max: Float = tempMax


    fun getDayTemp(): Float
    {
        return day
    }


    fun getMinTemp(): Float
    {
        return min
    }


    fun getMaxTemp(): Float
    {
        return max
    }

}