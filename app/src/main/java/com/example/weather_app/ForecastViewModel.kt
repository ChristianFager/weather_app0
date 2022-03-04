package com.example.weather_app
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class ForecastViewModel @Inject constructor(private val service: Api) : ViewModel()
{
    val forecastConditions: MutableLiveData<Forecast> = MutableLiveData()


    fun loadData() = runBlocking {
        launch {
            forecastConditions.value = service.getForecast("98178")
        }
    }

















    /*
    fun loadData()
    {
        val call: Call<Forecast> = service.getForecast("98178")
        call.enqueue(object : Callback<Forecast>
        {
            override fun onResponse
            (
                call: Call<Forecast>,
                response: Response<Forecast>
            )
            {
                val forecast = response.body()
                forecast?.let {
                    Log.e("TAG", "onResponse:"+it.toString())
                    //recyclerView.adapter = ForecastAdapter(it.list)
                    forecastConditions.value = it
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable)
            {
                Log.e("TAG", "onFailure: " + t.message)
            }
        })
    }
     */
}