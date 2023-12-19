package com.humbjorch.myapplication.sis.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.humbjorch.myapplication.data.model.LocationModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HelperGeolocation @Inject constructor(
    private val context: Context
) {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    fun isEnableGeolocation(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): LocationModel? {
        if (isLocationPermissionEnable())
            return null

        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        val location = fusedLocationClient.lastLocation.await()

        return if (location != null) {
            LocationModel(
                latitude = location.latitude,
                longitude = location.longitude,
                altitude = location.altitude
            )
        } else
            null
    }

    private fun isLocationPermissionEnable(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }
}