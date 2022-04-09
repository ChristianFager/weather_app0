package com.example.weather_app
import android.Manifest
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
                // use your location object
                // get latitude , longitude and other info from this
                lat = location.latitude
                lon = location.longitude
            }
        }
    }
}