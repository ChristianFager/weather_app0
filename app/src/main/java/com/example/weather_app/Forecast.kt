package com.example.weather_app
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Forecast : AppCompatActivity()
{
    private lateinit var recyclerView: RecyclerView


    private val adapterData = listOf(
        Data(1643403034138, 1643706034000, 1643752954000, ForecastTemp(75f, 70f, 90f), 10f, 20),
        Data(1643489434000, 1643706034000, 1643752774000, ForecastTemp(74f, 65f, 100f), 10f, 20),
        Data(1643575834000, 1643706034000, 1643752774000, ForecastTemp(73f, 85f, 105f), 10f, 20),
        Data(1643662234000, 1643706034000, 1643752774000, ForecastTemp(72f, 105f, 999f), 10f, 20),
        Data(1643748634000, 1644697667000, 1643752774000, ForecastTemp(71f, 68f, 78f), 10f, 20),
        Data(1643837267000, 1643706034000, 1643752774000, ForecastTemp(76f, 70f, 79f), 10f, 20),
        Data(1643923667000, 1644696527000, 1643752774000, ForecastTemp(75f, 71f, 77f), 10f, 20),
        Data(1644010067000, 1644697667000, 1643752774000, ForecastTemp(74f, 73f, 76f), 10f, 20),
        Data(1644096467000, 1643706034000, 1643752774000, ForecastTemp(72f, 64f, 75f), 10f, 20),
        Data(1644182867000, 1643706034000, 1643752774000, ForecastTemp(71f, 63f, 73f), 10f, 20),
        Data(1644269267000, 1644697667000, 1643752774000, ForecastTemp(69f, 62f, 74f), 10f, 20),
        Data(1644355667000, 1644696527000, 1643752774000, ForecastTemp(68f, 60f, 72f), 10f, 20),
        Data(1644442067000, 1644442067000, 1643752774000, ForecastTemp(67f, 59f, 71f), 10f, 20),
        Data(1644528467000, 1644528467000, 1643752774000, ForecastTemp(65f, 60f, 70f), 10f, 20),
        Data(1644614867000, 1644614867000, 1643752774000, ForecastTemp(64f, 69f, 75f), 10f, 20),
        Data(1644701267000, 1644701267000, 1643752774000, ForecastTemp(71f, 70f, 74f), 10f, 20),
    )


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(adapterData)

        //actionBar.getSupportActionBar.setTitle("Forecast")
        //val ab: ActionBar? = actionBar
        //ab.setTitle("Forecast")
    }
}