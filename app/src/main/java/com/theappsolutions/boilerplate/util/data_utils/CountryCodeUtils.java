package com.theappsolutions.boilerplate.util.data_utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.util.List;
import java.util.Locale;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class CountryCodeUtils {

    public static String getCountry(Context context) {
        String country = getCountryBasedOnSimCardOrNetwork(context);
        if (country == null) {
            country = getCountryFromLocationManager(context);
        }
        return country;
    }

    @SuppressLint("MissingPermission")
    @Nullable
    private static String getCountryFromLocationManager(Context context) {
        LocationManager locManager =
            (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locManager != null) {
            if (isGPSPermissionGranted(context)) {
                Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (location != null) {
                    Geocoder gcd = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);

                        if (addresses != null && !addresses.isEmpty()) {
                            String country = addresses.get(0).getCountryCode();
                            if (country != null) {
                                return country;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private static boolean isGPSPermissionGranted(Context context) {
        return isNoFineLocationPerm(context) && isNoCoarseLocPerm(context);
    }

    private static boolean isNoCoarseLocPerm(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean isNoFineLocationPerm(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    private static String getCountryBasedOnSimCardOrNetwork(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
