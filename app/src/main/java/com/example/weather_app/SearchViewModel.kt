package com.example.weather_app
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather_app.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import javax.inject.Inject
import kotlin.random.Random


class SearchViewModel @Inject constructor() : ViewModel()
{
    private var zipCode: String? = null
    private var lat: Double = 0.00
    private var lon: Double = 0.00
    private val _enableButton = MutableLiveData(false)
    val enableButton: LiveData<Boolean>
        get() = _enableButton

    private val _showErrorDialog = MutableLiveData(false)
    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog


    fun updateZipCode(zipCode: String)
    {
        if (zipCode != this.zipCode)
        {
            this.zipCode = zipCode
            _enableButton.value = isValidZipCode(zipCode)
        }
    }


    private fun isValidZipCode(zipCode: String): Boolean
    {
        return zipCode.length == 5 && zipCode.all {it.isDigit() }
    }


    fun submitButtonClicked()
    {
        //Make API Request//
        Log.d(MainViewModel::class.simpleName, zipCode ?: "No Zip Yet!")
        _showErrorDialog.value = Random.nextBoolean()
    }


    /*
    fun btnRequestLocation()
    {
        AlertDialog.Builder(this)
            .setMessage("Can we access GPS temporarily?")
            .setNeutralButton("OK") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_LOC_PERMISSION
                )
            }
            .create()
            .show()
    }
     */


    public fun getLat(): Double
    {
        /*
        val lm = getSystemService<Any>(Context.LOCATION_SERVICE) as LocationManager?
        val location: Location? = lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lat = location.getLatitude()
        return lat!!
         */

        return 31.9686
    }


    public fun getLon(): Double
    {
        /*
        val lm = getSystemService<Any>(Context.LOCATION_SERVICE) as LocationManager?
        val location: Location? = lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lon = location.getLongitude()
        return lon!!
         */

        return 99.9018
    }


    public fun getZip(): String
    {
        return zipCode!!
    }
}