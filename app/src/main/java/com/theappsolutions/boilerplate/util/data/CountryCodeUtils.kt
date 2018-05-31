package com.theappsolutions.boilerplate.util.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.support.v4.app.ActivityCompat
import android.telephony.TelephonyManager
import java.util.Locale

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Utils for country codes processing
 */
object CountryCodeUtils {

    fun getCountry(context: Context?): String? {
        var country = context?.let {
            getCountryBasedOnSimCardOrNetwork(it)
        }
        if (country == null) {
            country = context?.let {
                getCountryFromLocationManager(it)
            }
        }
        return country
    }

    @SuppressLint("MissingPermission")
    private fun getCountryFromLocationManager(context: Context): String? {
        val locManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (isGPSPermissionGranted(context)) {
            var location: Location? = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location == null) {
                location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
           location?.let {
                val gcd = Geocoder(context, Locale.getDefault())
                val addresses: List<Address>?
                try {
                    addresses = gcd.getFromLocation(it.latitude, it.longitude, 1)
                    if (addresses != null && !addresses.isEmpty()) {
                        val country = addresses.get(0).countryCode
                        if (country != null) {
                            return country
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    private fun isGPSPermissionGranted(context: Context): Boolean {
        return isNoFineLocationPerm(context) && isNoCoarseLocPerm(context)
    }

    private fun isNoCoarseLocPerm(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun isNoFineLocationPerm(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    private fun getCountryBasedOnSimCardOrNetwork(context: Context): String? {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simCountry = tm.simCountryIso
            when {
                simCountry != null && simCountry.length == 2 -> // SIM country code is available
                    return simCountry.toLowerCase(Locale.US)
                tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA -> { // device is not 3G (would be unreliable)
                    val networkCountry = tm.networkCountryIso
                    if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                        return networkCountry.toLowerCase(Locale.US)
                    }
                }
            }
        } catch (ignored: Exception) {
        }
        return null
    }
}
