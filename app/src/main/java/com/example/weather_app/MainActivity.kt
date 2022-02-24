package com.example.weather_app
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity()
{
    private val apiKey = "1a8e680481c1a649e85fafc805abee10"
    private lateinit var api: Api
    private lateinit var cityName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var lowTemp: TextView
    private lateinit var highTemp: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView
    private lateinit var feelsLike: TextView
    private lateinit var conditionIcon: ImageView


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityName = findViewById(R.id.city_name)
        currentTemp = findViewById(R.id.temperature)
        lowTemp = findViewById(R.id.tempMin)
        highTemp = findViewById(R.id.tempMax)
        humidity = findViewById(R.id.humidity)
        pressure = findViewById(R.id.pressure)
        feelsLike = findViewById(R.id.feels_like)
        conditionIcon = findViewById(R.id.condition_icon)

        val button = findViewById<Button>(R.id.btnForecast)
        button.setOnClickListener{
            val intent = Intent(this, ForecastActivity::class.java)
            startActivity(intent)
        }

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)
    }


    override fun onResume()
    {
        super.onResume()
        val call: Call<CurrentConditions> = api.getCurrentConditions("98178")
        call.enqueue(object : Callback<CurrentConditions>
        {
            override fun onResponse
            (
                call: Call<CurrentConditions>,
                response: Response<CurrentConditions>
            )
            {
              val currentConditions = response.body()
                currentConditions?.let {
                    bindData(it)
                }
            }

            override fun onFailure(call: Call<CurrentConditions>, t: Throwable)
            {
                //
            }
        })
    }


    private fun bindData(currentConditions: CurrentConditions)
    {
        cityName.text = currentConditions.name
        currentTemp.text = getString(R.string.temperature, currentConditions.main.temp.toInt())
        lowTemp.text = "Low " + getString(R.string.tempMin, currentConditions.main.tempMin.toInt())
        highTemp.text = "High " + getString(R.string.tempMax, currentConditions.main.tempMax.toInt())
        humidity.text = "Humidity " + getString(R.string.humidity, currentConditions.main.humidity.toInt()) + "%"
        pressure.text = "Pressure " + getString(R.string.pressure, currentConditions.main.pressure.toInt()) + " hPa"
        feelsLike.text = "Feels like " + getString(R.string.feels_like, currentConditions.main.feelsLike.toInt())
        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(conditionIcon)
    }
}