package com.example.weather_app
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.weather_app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnForecast.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }
    }


    override fun onResume()
    {
        super.onResume()
        viewModel.currentConditions.observe(this)
        {
            CurrentConditions -> bindData(CurrentConditions)
        }

        viewModel.loadData()
    }


    @SuppressLint("SetTextI18n")
    private fun bindData(currentConditions: CurrentConditions)
    {
        binding.cityName.text = currentConditions.name
        binding.temperature.text = getString(R.string.temperature, currentConditions.main.temp.toInt())
        binding.tempMin.text = "Low " + getString(R.string.tempMin, currentConditions.main.tempMin.toInt())
        binding.tempMax.text = "High " + getString(R.string.tempMax, currentConditions.main.tempMax.toInt())
        binding.humidity.text = "Humidity " + getString(R.string.humidity, currentConditions.main.humidity.toInt()) + "%"
        binding.pressure.text = "Pressure " + getString(R.string.pressure, currentConditions.main.pressure.toInt()) + " hPa"
        binding.feelsLike.text = "Feels like " + getString(R.string.feels_like, currentConditions.main.feelsLike.toInt())
        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.conditionIcon)
    }
}