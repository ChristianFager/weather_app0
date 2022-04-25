package com.example.weather_app

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.weather_app.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationsClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val REQUEST_PERMISSION_LOC_STATE = 1
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    var lat: Double = 32.7157
    var lon: Double = -117.1611
    var notifications_active: Boolean = false;


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        {
            permissions ->
            when
            {
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    requestLocationUpdates()
                }
                else ->
                {
                    // No location access granted.
                }
            }
        }
        fusedLocationsClient = LocationServices.getFusedLocationProviderClient(this)
    }


    override fun onResume()
    {
        super.onResume()
        requestLocationUpdates()
    }


    fun ultimate_request_location()  //Tried every example I found, none are giving lat/lon
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 2)
        }
        fusedLocationsClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationsClient.lastLocation.addOnSuccessListener {
            requestNewLocation()
            if (it != null)
            {
                Log.d("TAG", it.toString())
                lat = it.latitude
                lon = it.longitude
            }
        }
    }


    fun requestLocation()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            AlertDialog.Builder(this)
                .setTitle("Request")
                .setTitle("Rationale")
                .setNeutralButton("Ok")
                { _, _ ->
                    locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
                }
                .show()
        }
        else
        {
            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }


    fun requestLocationUpdates()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            request_permissions()
            return
        }
        fusedLocationsClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationsClient.lastLocation.addOnSuccessListener {
                requestNewLocation()
                Log.d("MainActivity", it.toString())
            }
    }


    fun requestNewLocation()
    {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 0L
        locationRequest.fastestInterval = 0L
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        val locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(locationResult: LocationResult)
            {
                locationResult.locations.forEach {
                    // request weather data for location
                    lat = locationResult.lastLocation.latitude
                    lon = locationResult.lastLocation.longitude
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            request_permissions()
            return
        }
        locationProvider.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }


    fun showAlertLocation()
    {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("Your location settings is set to Off, Please enable location to use this application")
        dialog.setPositiveButton("Settings") { _, _ ->
            val myIntent = Intent(ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(myIntent)
        }
        dialog.setNegativeButton("Cancel") { _, _ ->
            finish()
        }
        dialog.setCancelable(false)
        dialog.show()
    }


    fun request_permissions()
    {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_PERMISSION_LOC_STATE)
            }
            else
            {
                requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_PERMISSION_LOC_STATE)
            }
        }
        else
        {
            Toast.makeText(this@MainActivity, "Permission (already) Granted!", Toast.LENGTH_SHORT)
                .show()
        }
    }


    fun showExplanation(title: String, message: String, permission: String, permissionRequestCode: Int)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton(R.string.ok)
        {
            dialog, id -> requestPermission(permission, permissionRequestCode)
        }
        builder.create().show()
    }


    fun requestPermission(permissionName: String, permissionRequestCode: Int)
    {
        ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestCode)
    }


    fun getLastKnownLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            request_permissions()
        }

        locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(locationResult: LocationResult)
            {
                locationResult ?: return
                for (location in locationResult.locations)
                {
                    lat = location.latitude
                    lon = location.longitude
                }
            }
        }

        fusedLocationsClient.lastLocation.addOnSuccessListener { location->
            if (location != null)
            {
                lat = location.latitude
                lon = location.longitude
            }
        }
    }


    fun make_notification()
    {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val id: String = "my_channel_01"
        val notification_id = 101

        val weather_text: String = "Weather Updated"
        var builder = NotificationCompat.Builder(this, id)
            //.setSmallIcon(R.drawable.notification_icon)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentTitle("Weather App")
            .setContentText(weather_text)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(weather_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notification_id, builder.build())
        }
    }


    fun createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val name = "channel0"
            val descriptionText = "channel_test" //getString(R.string.common_google_play_services_notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("my_channel_01", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun start_service()
    {
        notifications_active = true;

        val intent = Intent(this, service::class.java)
        val pintent = PendingIntent.getService(this, 0, intent, 0)
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            (30 * 1000).toLong(),
            pintent
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun stop_service()
    {
        notifications_active = false;
        val intent = Intent(this, service::class.java)
        val sender = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
        stopService(Intent(applicationContext, service::class.java))
    }
}