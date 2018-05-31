package com.theappsolutions.boilerplate.util.other.play_services

import android.content.Context
import android.content.res.Resources

import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException

import io.reactivex.Single
import timber.log.Timber

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Provide access to Google Advertising ID
 */
object GaidRetriever {

    private fun getGaid(context: Context): String? {
        var idInfo: AdvertisingIdClient.Info? = null
        try {
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
        } catch (e: GooglePlayServicesNotAvailableException) {
            Timber.e(e)
        } catch (e: GooglePlayServicesRepairableException) {
            Timber.e(e)
        } catch (e: Exception) {
            Timber.e(e)
        }

        var advertId: String? = null
        try {
            advertId = idInfo?.id
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return advertId
    }

    fun getGaidObservable(context: Context): Single<String> {
        return Single.create { e ->
            val gaid = getGaid(context)
            Timber.d("GAID: %s", gaid)
            when (gaid) {
                null -> e.onError(Resources.NotFoundException())
                else -> e.onSuccess(gaid)
            }
        }
    }

}
