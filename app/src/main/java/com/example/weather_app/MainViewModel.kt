package com.example.weather_app
import androidx.lifecycle.ViewModel
import javax.inject.Inject


class MainViewModel @Inject constructor(private val service: Api) : ViewModel()
{
}