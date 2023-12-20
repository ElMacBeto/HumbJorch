package com.humbjorch.myapplication.sis.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class HelperGeolocation @Inject constructor(
    private val context: Context
) {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun isEnableGeolocation(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    suspend fun getUserLocation(): Location? {
        val locationProvide = LocationServices.getFusedLocationProviderClient(context)
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsActive =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )

        if (!context.isPermissionGranted() || !isGpsActive) {
            return null
        }
        return suspendCancellableCoroutine { con ->
            locationProvide.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        con.resume(result) {}
                    } else {
                        con.resume(null) {}
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    con.resume(it) {}
                }
                addOnFailureListener {
                    con.resume(null) {}
                }
                addOnCanceledListener {
                    con.resume(null) {}
                }
            }
        }
    }
}