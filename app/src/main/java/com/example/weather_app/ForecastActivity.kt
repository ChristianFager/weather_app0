package com.example.weather_app
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class ForecastActivity : AppCompatActivity()
{
    private lateinit var recyclerView: RecyclerView
    //private val apiKey = "1a8e680481c1a649e85fafc805abee10"
    private lateinit var api: Api
    //private lateinit var dayDate: TextView
    //private lateinit var dayTemp: TextView
    //private lateinit var dayLowTemp: TextView
    //private lateinit var dayHighTemp: TextView
    //private lateinit var daySunrise: TextView
    //private lateinit var daySunset: TextView
    //private lateinit var dayConditionIcon: ImageView


    /*
    private val adapterData = listOf(
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        DayForecast(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
    )
     */


    fun onResume(savedInstanceState: Bundle?)
    {
        val call: Call<Forecast> = api.getForecast("98178")
        Log.e("TAG", "TEST0")
        call.enqueue(object : Callback<Forecast>
        {
            override fun onResponse
                        (
                call: Call<Forecast>,
                response: Response<Forecast>
            )
            {
                Log.e("TAG", "TEST1")
                val forecast = response.body()
                forecast?.let {
                    Log.e("TAG", "onResponse:"+it.toString() )
                    recyclerView.adapter = MyAdapter(it.list) //MyAdapter(listOf(it)) //it or it.list?
                    //bindData(it)
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable)
            {
                Log.e("TAG", "TEST2")
                Log.e("TAG", "onFailure: " + t.message)
            }
        })    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.adapter = MyAdapter(adapterData)

        /*
        dayTemp = findViewById(R.id.tempDay)
        dayLowTemp = findViewById(R.id.tempMin)
        dayHighTemp = findViewById(R.id.tempMax)
        dayDate = findViewById(R.id.monthDay)
        daySunrise = findViewById(R.id.sunrise)
        daySunset = findViewById(R.id.sunset)
        dayConditionIcon = findViewById(R.id.iconDay)
         */

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)
        val call: Call<Forecast> = api.getForecast("98178")
        Log.e("TAG", "TEST0")
        call.enqueue(object : Callback<Forecast>
        {
            override fun onResponse
            (
                call: Call<Forecast>,
                response: Response<Forecast>
            )
            {
                Log.e("TAG", "TEST1")
                val forecast = response.body()
                forecast?.let {
                    Log.e("TAG", "onResponse:"+it.toString() )
                    recyclerView.adapter = MyAdapter(it.list) //MyAdapter(listOf(it)) //it or it.list?
                    //bindData(it)
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable)
            {
                Log.e("TAG", "TEST2")
                Log.e("TAG", "onFailure: " + t.message)
            }
        })
    }


    /*
    private fun bindData(forecast: Forecast)
    {
        dayDate.text = getString(R.string.date)
        dayTemp.text = "{forecast.dayTemp}°"
        dayLowTemp.text = "Low" + "{forecast.dayLowTemp}°"
        dayHighTemp.text = "High" + "{forecast.dayHighTemp}°"
        daySunrise.text = "Sunrise" + "{forecast.daySunrise}" + "%"
        daySunset.text = "Sunrise" + "{forecast.daySunset}" + "%"
        val iconName = dayConditionIcon //forecast.dayConditionIcon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(dayConditionIcon)
    }
     */
}