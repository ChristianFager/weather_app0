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
    private lateinit var api: Api


    fun onResume(savedInstanceState: Bundle?)
    {
        val call: Call<Forecast> = api.getForecast("98178")
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
                    Log.e("TAG", "onResponse:"+it.toString() )
                    recyclerView.adapter = MyAdapter(it.list)
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable)
            {
                Log.e("TAG", "onFailure: " + t.message)
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.adapter = MyAdapter(adapterData) //Old manual data//

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pro.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)
        val call: Call<Forecast> = api.getForecast("98178")
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
                    Log.e("TAG", "onResponse:"+it.toString() )
                    recyclerView.adapter = MyAdapter(it.list) //MyAdapter(listOf(it)) //it or it.list?
                    //bindData(it)
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable)
            {
                Log.e("TAG", "onFailure: " + t.message)
            }
        })
    }
}