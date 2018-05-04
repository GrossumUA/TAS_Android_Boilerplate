package com.theappsolutions.boilerplate.util.other.play_services;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import io.reactivex.Single;
import timber.log.Timber;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Provide access to Google Advertising ID
 */
public class GaidRetriever {

    private static String getGaid(Context context) {
        AdvertisingIdClient.Info idInfo = null;
        try {
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
        } catch (GooglePlayServicesNotAvailableException e) {
            Timber.e(e);
        } catch (GooglePlayServicesRepairableException e) {
            Timber.e(e);
        } catch (Exception e) {
            Timber.e(e);
        }
        String advertId = null;
        try {
            advertId = idInfo.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return advertId;
    }

    public static Single<String> getGaidObservable(Context context) {
        return Single.create(e -> {
            String gaid = getGaid(context);
            Timber.d("GAID: %s", gaid);
            if (gaid == null)
                e.onError(new Resources.NotFoundException());
            else
                e.onSuccess(gaid);
        });
    }

}
