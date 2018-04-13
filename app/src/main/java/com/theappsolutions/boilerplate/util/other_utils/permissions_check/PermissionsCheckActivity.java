package com.theappsolutions.boilerplate.util.other_utils.permissions_check;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate.ui.base.BaseMvpActivity;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.theappsolutions.boilerplate.TasBoilerplateSettings.NECESSARY_PERMISSIONS;


/**
 * Created by Dmytro Yakovlev.
 */

public abstract class PermissionsCheckActivity extends BaseMvpActivity {

    public static final String SUPPRESS_PERMISSIONS = "suppress_permissions";

    private static final int OVERLAY_PERMISSION_REQ_CODE = 5345;
    private static final int REQUEST_PERMISSION_SETTING = 5346;
    private static final int ASK_PERMISSION_REQ_CODE = 5347;

    private boolean isNeedToShowSettings = false;
    private boolean isAllPermissionsGranted = true;
    private boolean isGoAheadInOnResume = false;

    public abstract void goAheadAfterPermissionsEnabling();

    protected void processPermissionAfterStart() {
        if (getIntent().getBooleanExtra(SUPPRESS_PERMISSIONS, false)) {
            goAheadAfterPermissionsEnabling();
            return;
        }
        PermissionsUtils helper =
                new PermissionsUtils(this, NECESSARY_PERMISSIONS);
        List<String> neededPerms = helper.getNeededPermissions();
        if (neededPerms.size() == 0) {
            Timber.d("Go to main screen");
            goAheadAfterPermissionsEnabling();
        } else {
            Timber.d("getIntent request permissions");
            ActivityCompat.requestPermissions(this,
                    neededPerms.toArray(new String[neededPerms.size()]),
                    ASK_PERMISSION_REQ_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Timber.d("onRequestPermissionsResult works");
        List<String> notGrantedPerm = new ArrayList<>();
        if (requestCode == ASK_PERMISSION_REQ_CODE) {

            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    isAllPermissionsGranted = false;
                    // user rejected the permission
                    boolean showRationale = ActivityCompat
                            .shouldShowRequestPermissionRationale(
                                    this, permission);
                    if (!showRationale &&
                            !permission.equals(
                                    Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                        isNeedToShowSettings = true;
                        break;

                    } else {
                        notGrantedPerm.add(permission);
                    }
                }
            }

            if (isOnlyOverlayPermNeeded(notGrantedPerm)) {
                requestOverlayPermission();
            } else {
                if (isNeedToShowSettings) {
                    Timber.d("Detected [Never ask again]");
                    openSettings();
                } else if (isAllPermissionsGranted) {
                    isGoAheadInOnResume = true;
                } else {
                    isAllPermissionsGranted = true;
                    Timber.d(
                            "Not granted amount: %s", notGrantedPerm.size());
                    ActivityCompat.requestPermissions(this,
                            notGrantedPerm.toArray(
                                    new String[notGrantedPerm.size()]),
                            ASK_PERMISSION_REQ_CODE);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGoAheadInOnResume) {
            goAheadAfterPermissionsEnabling();
            isGoAheadInOnResume = false;
        }
    }

    private boolean isOnlyOverlayPermNeeded(List<String> perm) {
        return perm.contains(
                Manifest.permission.SYSTEM_ALERT_WINDOW) && perm.size() == 1;
    }

    private void openSettings() {
        Timber.d("Open settings dialog");
        final Intent i = new Intent();
        i.setAction(Settings
                .ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivityForResult(i, REQUEST_PERMISSION_SETTING);

        showToastMessage(R.string.msg_permissions_enabling);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestOverlayPermission() {
        showToastMessage(getString(R.string.msg_permit_drawing));
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        //wait for onActivityResult()
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            Timber.d("onActivityResult REQUEST_PERMISSION_SETTING");
            checkPermissions();
        } else if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            boolean overlayPermissionGranted = Settings.canDrawOverlays(this);
            Timber.d("Granted overlay: %s", overlayPermissionGranted);
            checkPermissions();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkPermissions() {
        PermissionsUtils helper = new PermissionsUtils(this, NECESSARY_PERMISSIONS);
        List<String> grantedPermissions = helper.getGrantedPermissions();
        int size = grantedPermissions.size();
        Timber.d("Size: %s", size);
        if (size == NECESSARY_PERMISSIONS.length) {
            goAheadAfterPermissionsEnabling();
        } else if (isOnlyOverlayPermNeeded(helper.getNeededPermissions())) {
            requestOverlayPermission();
        } else {
            openSettings();
        }
    }
}
