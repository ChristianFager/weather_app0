package com.example.weather_app
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class ForecastViewModel @Inject constructor(private val service: Api) : ViewModel()
{
    val forecastConditions: MutableLiveData<Forecast> = MutableLiveData()


    fun loadData(tzip: String) = runBlocking {
        launch {
            forecastConditions.value = service.getForecast(tzip)
        }
    }


    fun loadLatLong(lat: Double, lon: Double) = runBlocking {
        launch {
            forecastConditions.value = service.getForecastLatLong(lat, lon)
        }
    }
}