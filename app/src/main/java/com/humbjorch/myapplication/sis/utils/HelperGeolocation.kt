package com.humbjorch.myapplication.sis.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        var permissions = arrayOf<String>()

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions = permissions.plus(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions = permissions.plus(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }

        return permissions.isNotEmpty()
    }
}