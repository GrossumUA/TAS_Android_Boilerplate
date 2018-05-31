package com.theappsolutions.boilerplate.util.other

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager

import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.other.exceptions.NoConnectionException

import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections

import retrofit2.HttpException
import kotlin.experimental.and

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
/**
 * Returns true if the Throwable is an instance of RetrofitError with an
 * http status code equals to the given one.
 */
fun isHttpStatusCode(throwable: Throwable, statusCode: Int): Boolean {
    return throwable is HttpException && throwable.code() == statusCode
}

fun isNetworkConnected(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var activeNetwork: NetworkInfo? = null
    if (cm != null) {
        activeNetwork = cm.activeNetworkInfo
    }
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}

fun noConnectionException(context: Context): NoConnectionException {
    return NoConnectionException(context
            .resources.getString(R.string.err_no_internet_connection))
}

/**
 * Get the network info
 */
fun getNetworkInfo(context: Context): NetworkInfo? {
    val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo
}

/**
 * Check if there is any connectivity
 */
fun isConnected(context: Context): Boolean {
    val info = getNetworkInfo(context)
    return info != null && info.isConnected
}

/**
 * Check if there is any connectivity to a Wifi network
 */
fun isConnectedWifi(context: Context): Boolean {
    val info = getNetworkInfo(context)
    return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
}

/**
 * Check if there is any connectivity to a mobile network
 */
fun isConnectedMobile(context: Context): Boolean {
    val info = getNetworkInfo(context)
    return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
}

/**
 * Check if there is fast connectivity
 */
fun isConnectedFast(context: Context): Boolean {
    val info = getNetworkInfo(context)
    return info != null && info.isConnected &&
            isConnectionFast(info.type, info.subtype)
}

/**
 * Check if the connection is fast
 */
fun isConnectionFast(type: Int, subType: Int): Boolean {
    return if (type == ConnectivityManager.TYPE_WIFI) {
        true
    } else if (type == ConnectivityManager.TYPE_MOBILE) {
        when (subType) {
            TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
            TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
            TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> true // ~ 400-1000 kbps
            TelephonyManager.NETWORK_TYPE_EVDO_A -> true // ~ 600-1400 kbps
            TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
            TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
            TelephonyManager.NETWORK_TYPE_HSPA -> true // ~ 700-1700 kbps
            TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
            TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps
        /*
         * Above API level 7, make sure to set android:targetSdkVersion
         * to appropriate level to use these
         */
            TelephonyManager.NETWORK_TYPE_EHRPD // API level 11
            -> true // ~ 1-2 Mbps
            TelephonyManager.NETWORK_TYPE_EVDO_B // API level 9
            -> true // ~ 5 Mbps
            TelephonyManager.NETWORK_TYPE_HSPAP // API level 13
            -> true // ~ 10-20 Mbps
            TelephonyManager.NETWORK_TYPE_IDEN // API level 8
            -> false // ~25 kbps
            TelephonyManager.NETWORK_TYPE_LTE // API level 11
            -> true // ~ 10+ Mbps
        // Unknown
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
            else -> false
        }
    } else {
        false
    }
}

/**
 * Convert byte array to hex string
 *
 * @param bytes
 * @return
 */
fun bytesToHex(bytes: ByteArray): String {
    val sbuf = StringBuilder()
    for (idx in bytes.indices) {
        val intVal = bytes[idx] and 0xff.toByte()
        if (intVal < 0x10) sbuf.append("0")
        sbuf.append(Integer.toHexString(intVal.toInt()).toUpperCase())
    }
    return sbuf.toString()
}

/**
 * Get utf8 byte array.
 *
 * @param str
 * @return array of NULL if error was found
 */
fun getUTF8Bytes(str: String): ByteArray? {
    try {
        return str.toByteArray(charset("UTF-8"))
    } catch (ex: Exception) {
        return null
    }

}

/**
 * Load UTF8withBOM or any ansi text file.
 *
 * @param filename
 * @return
 * @throws java.io.IOException
 */
@Throws(java.io.IOException::class)
fun loadFileAsString(filename: String): String {
    val BUFLEN = 1024
    val bufferedInputStream = BufferedInputStream(FileInputStream(filename), BUFLEN)
    try {
        val baos = ByteArrayOutputStream(BUFLEN)
        val bytes = ByteArray(BUFLEN)
        var isUTF8 = false
        var read: Int = bufferedInputStream.read(bytes);
        var count = 0

        while (read != -1) {
            if (count == 0 && bytes[0] == 0xEF.toByte() && bytes[1] == 0xBB.toByte() && bytes[2] == 0xBF.toByte()) {
                isUTF8 = true
                baos.write(bytes, 3, read - 3) // drop UTF8 bom marker
            } else {
                baos.write(bytes, 0, read)
            }
            count += read
        }
        return if (isUTF8) {
            String(baos.toByteArray(), charset("UTF-8"))
        } else {
            String(baos.toByteArray())
        }
    } finally {
        try {
            bufferedInputStream.close()
        } catch (ex: Exception) {
        }
    }
}

/**
 * Returns MAC address of the given interface name.
 *
 * @param interfaceName eth0, wlan0 or NULL=use first interface
 * @return mac address or empty string
 */
fun getMACAddress(interfaceName: String?): String {
    try {
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            if (interfaceName != null) {
                if (!intf.name.equals(interfaceName, ignoreCase = true)) {
                    continue
                }
            }
            val mac = intf.hardwareAddress ?: return ""
            val buf = StringBuilder()
            for (idx in mac.indices) {
                buf.append(String.format("%02X:", mac[idx]))
            }
            if (buf.length > 0) {
                buf.deleteCharAt(buf.length - 1)
            }
            return buf.toString()
        }
    } catch (ex: Exception) {
    }
    // for now eat exceptions
    return ""
}

/**
 * Get IP address from first non-localhost interface
 *
 *
 * ipv4 true=return ipv4, false=return ipv6
 *
 * @return address or empty string
 */
fun getIPAddress(useIPv4: Boolean): String {
    try {
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            val addrs = Collections.list(intf.inetAddresses)
            for (addr in addrs) {
                if (!addr.isLoopbackAddress) {
                    val sAddr = addr.hostAddress
                    //boolean isIPv4 = InetAddressUtils.isIPv4Address
                    // (sAddr);
                    val isIPv4 = sAddr.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) {
                            return sAddr
                        }
                    } else {
                        if (!isIPv4) {
                            val delim = sAddr.indexOf('%') // drop ip6
                            // zone suffix
                            return if (delim < 0) {
                                sAddr.toUpperCase()
                            } else {
                                sAddr.substring(0, delim).toUpperCase()
                            }
                        }
                    }
                }
            }
        }
    } catch (ex: Exception) {
    }
    // for now eat exceptions
    return ""
}
