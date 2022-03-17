package com.example.weather_app
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class CurrentConditionsViewModel @Inject constructor(private val service: Api) : ViewModel()
{
    public val currentConditions: MutableLiveData<CurrentConditions> = MutableLiveData()


    fun loadData(tzip: String) = runBlocking {
        launch {
            currentConditions.value = service.getCurrentConditions(tzip)
        }
    }
}