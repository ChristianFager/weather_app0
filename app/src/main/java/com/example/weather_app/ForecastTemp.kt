package com.example.weather_app
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ForecastTemp(
    val day: Float,
    val min: Float,
    val max: Float
    ) : Parcelable
