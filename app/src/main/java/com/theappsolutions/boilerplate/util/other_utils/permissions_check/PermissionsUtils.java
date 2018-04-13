package com.theappsolutions.boilerplate.util.other_utils.permissions_check;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class PermissionsUtils {

    private static final SparseArray<PermissionsUtils> INSTANCES = new SparseArray<>();
    private static volatile int sNextRequestCode;

    private final List<String> necessaryPermissions = new ArrayList<>();
    private final List<String> grantedPermissions = new ArrayList<>();

    private final Context mContext;

    public PermissionsUtils(Context context, String[] necessaryPermissions) {
        this.necessaryPermissions.addAll(Arrays.asList(necessaryPermissions));
        mContext = context;
    }

    @TargetApi(23)
    private boolean checkOverlay() {
        return Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(mContext);
    }

    public List<String> getGrantedPermissions() {

        final List<String> mGrantedPermissions = new ArrayList<>();
        for (String permission : necessaryPermissions) {
            if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW) &&
                    checkOverlay()) {
                mGrantedPermissions.add(Manifest.permission
                        .SYSTEM_ALERT_WINDOW);
            } else if (ContextCompat.checkSelfPermission(mContext,
                    permission) == PackageManager.PERMISSION_GRANTED) {
                mGrantedPermissions.add(permission);
            }
        }
        return mGrantedPermissions;
    }

    public List<String> getNeededPermissions() {
        final List<String> neededPermissions = new ArrayList<>();
        for (String permission : necessaryPermissions) {
            if (ContextCompat.checkSelfPermission(mContext,
                    permission) == PackageManager.PERMISSION_DENIED) {

                if (permission.equals(Manifest.permission
                        .SYSTEM_ALERT_WINDOW)) {
                    if (!checkOverlay()) {
                        neededPermissions.add(permission);
                    }
                } else {
                    neededPermissions.add(permission);
                }

            }
        }
        return neededPermissions;
    }

    private static synchronized PermissionsUtils getInstance(int code) {
        PermissionsUtils instance = INSTANCES.get(code);
        INSTANCES.remove(code);
        return instance;
    }

    private static synchronized int saveInstance(PermissionsUtils instance) {
        int code = sNextRequestCode++;
        INSTANCES.put(code, instance);
        return code;
    }

    static void onRequestPermissionsResult(
            String[] permissions, int[] grantResults, int requestCode) {
        getInstance(requestCode).onRequestPermissionsResult(permissions, grantResults);
    }

    private synchronized void onRequestPermissionsResult(String[] permissions,
                                                         int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permissions[i]);
            }
        }

        notifyAll();
    }

    public static void openSettings(Activity activity) {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + activity.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(i);
    }

}
