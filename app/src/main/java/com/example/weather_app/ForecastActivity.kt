package com.example.weather_app
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.databinding.ActivityForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ForecastActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityForecastBinding
    @Inject lateinit var viewModel: ForecastViewModel


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }


    override fun onResume()
    {
        super.onResume()
        viewModel.forecastConditions.observe(this)
        {
            binding.recyclerView.adapter = ForecastAdapter(it.list)
        }
        viewModel.loadData()
    }
}