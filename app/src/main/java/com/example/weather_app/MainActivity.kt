package com.example.weather_app

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weather_app.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationsClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val REQUEST_PERMISSION_LOC_STATE = 1


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        fusedLocationsClient = LocationServices.getFusedLocationProviderClient(this)
        //showAlertLocation()
        //getLastKnownLocation()
        /*
        val src = CancellationTokenSource()
        var ct: CancellationToken? = src.token
        Log.e("TEST", fusedLocationsClient.getCurrentLocation(ct))
        fusedLocationsClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                Log.e("LOCATION", location.toString())
            }


        //Manifest.permission.ACCESS_COARSE_LOCATION = PackageManager.PERMISSION_GRANTED.toString()
        Log.e("PERMISSION", PackageManager.PERMISSION_GRANTED.toString())
         */

        /*
        locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(locationResult: LocationResult?)
            {
                locationResult ?: return
                for (location in locationsResult.locations)
                {
                    //Update UI with location data//
                }
            }
        }
         */
    }


    public fun showAlertLocation()
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


    private fun request_permissions()
    {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            {
                showExplanation(
                    "Permission Needed",
                    "Rationale",
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    REQUEST_PERMISSION_LOC_STATE
                )
            }
            else
            {
                requestPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    REQUEST_PERMISSION_LOC_STATE
                )
            }
        }
        else
        {
            Toast.makeText(this@MainActivity, "Permission (already) Granted!", Toast.LENGTH_SHORT)
                .show()
        }
    }


    /*
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_LOC_STATE -> if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this@MainActivity, "Permission Granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
     */


    private fun showExplanation(
        title: String,
        message: String,
        permission: String,
        permissionRequestCode: Int
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                R.string.ok
            ) { dialog, id -> requestPermission(permission, permissionRequestCode) }
        builder.create().show()
    }

    private fun requestPermission(permissionName: String, permissionRequestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestCode)
    }


    public fun getLastKnownLocation()
    {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
        {
            request_permissions()
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationsClient.lastLocation.addOnSuccessListener { location->
                if (location != null)
                {
                    // use your location object
                    // get latitude , longitude and other info from this
                    Log.e("TEST", location.latitude.toString());
                    Log.e("TEST", location.longitude.toString());
                }

            }
    }


    /*
    override fun onResume()
    {
        super.onResume()
        if (requestingLocationUpdates)
            startLocationUpdates()
    }


    private fun startLocationUpdates()
    {
        fusedLocationsClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
     */
}