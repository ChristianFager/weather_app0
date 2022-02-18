package com.example.weather_app
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


class MyAdapter(private val data: List<DayForecast>) : RecyclerView.Adapter<MyAdapter.ViewHolder>()
{
    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        //private val formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mma")
        private val formatterHour = DateTimeFormatter.ofPattern("h:mma")
        private val formatterMonthDay = DateTimeFormatter.ofPattern("MMM dd")
        private val dateView: TextView = view.findViewById(R.id.monthDay)
        private val tempView: TextView = view.findViewById(R.id.tempDay)
        private val maxView: TextView = view.findViewById(R.id.tempMax)
        private val minView: TextView = view.findViewById(R.id.tempMin)
        private val sunriseView: TextView = view.findViewById(R.id.sunrise)
        private val sunsetView: TextView = view.findViewById(R.id.sunset)


        fun bind(data: DayForecast)
        {
            Log.e("TEST3", "bind: "+ data.date)
            Log.e("TAG", "bind: "+ data.date)
            val instant = Instant.ofEpochMilli(data.date!!)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            dateView.text = formatterMonthDay.format(dateTime)

            val tDay = "Temp: " + data.temp!!.day.roundToInt() + "°"
            tempView.text = tDay
            val tMax = "High: " + data.temp.max.roundToInt() + "°"
            maxView.text = tMax
            val tMin = "Low: " + data.temp.min.roundToInt() + "°"
            minView.text = tMin

            val instantSunrise = Instant.ofEpochMilli(data.sunrise)
            val sunrise = LocalDateTime.ofInstant(instantSunrise, ZoneId.systemDefault())
            val tSunrise = "Sunrise: " + formatterHour.format(sunrise)
            sunriseView.text = tSunrise

            val instantSunset = Instant.ofEpochMilli(data.sunset)
            val sunset = LocalDateTime.ofInstant(instantSunset, ZoneId.systemDefault())
            val tSunset = "Sunset: " + formatterHour.format(sunset)
            sunsetView.text = tSunset
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view =  LayoutInflater.from(parent.context)
            .inflate(R.layout.row_date, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(data[position])
    }


    override fun getItemCount() = data.size
}