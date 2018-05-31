package com.theappsolutions.boilerplate.util.other.permissions_check

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat

import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.TasBoilerplateSettings
import com.theappsolutions.boilerplate.ui.base.BaseMvpActivity

import java.util.ArrayList

import timber.log.Timber


/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Base activity for screens with permission checking
 */
abstract class PermissionsCheckActivity : BaseMvpActivity() {

    private var isNeedToShowSettings = false
    private var isAllPermissionsGranted = true
    private var isGoAheadInOnResume = false

    abstract fun goAheadAfterPermissionsEnabling()

    protected fun processPermissionAfterStart() {
        if (intent.getBooleanExtra(SUPPRESS_PERMISSIONS, false)) {
            goAheadAfterPermissionsEnabling()
            return
        }
        val helper = PermissionsUtils(this, TasBoilerplateSettings.NECESSARY_PERMISSIONS)
        val neededPerms = helper.getNeededPermissions()
        when {
            neededPerms.size == 0 -> {
                Timber.d("Go to main screen")
                goAheadAfterPermissionsEnabling()
            }
            else -> {
                Timber.d("getIntent request permissions")
                ActivityCompat.requestPermissions(this,
                        neededPerms.toTypedArray(),
                        ASK_PERMISSION_REQ_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Timber.d("onRequestPermissionsResult works")
        val notGrantedPerm = ArrayList<String>()
        if (requestCode == ASK_PERMISSION_REQ_CODE) {
            var i = 0
            val len = permissions.size
            while (i < len) {
                val permission = permissions[i]
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    isAllPermissionsGranted = false
                    // user rejected the permission
                    val showRationale = ActivityCompat
                            .shouldShowRequestPermissionRationale(
                                    this, permission)
                    if (!showRationale && permission != Manifest.permission.SYSTEM_ALERT_WINDOW) {
                        isNeedToShowSettings = true
                        break

                    } else {
                        notGrantedPerm.add(permission)
                    }
                }
                i++
            }

            if (isOnlyOverlayPermNeeded(notGrantedPerm)) {
                requestOverlayPermission()
            } else {
                when {
                    isNeedToShowSettings -> {
                        Timber.d("Detected [Never ask again]")
                        openSettings()
                    }
                    isAllPermissionsGranted -> isGoAheadInOnResume = true
                    else -> {
                        isAllPermissionsGranted = true
                        Timber.d(
                                "Not granted amount: %s", notGrantedPerm.size)
                        ActivityCompat.requestPermissions(this,
                                notGrantedPerm.toTypedArray(),
                                ASK_PERMISSION_REQ_CODE)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isGoAheadInOnResume) {
            goAheadAfterPermissionsEnabling()
            isGoAheadInOnResume = false
        }
    }

    private fun isOnlyOverlayPermNeeded(perm: List<String>): Boolean {
        return perm.contains(
                Manifest.permission.SYSTEM_ALERT_WINDOW) && perm.size == 1
    }

    private fun openSettings() {
        Timber.d("Open settings dialog")
        val i = Intent()
        i.action = Settings
                .ACTION_APPLICATION_DETAILS_SETTINGS
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:$packageName")
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivityForResult(i, REQUEST_PERMISSION_SETTING)

        showToastMessage(R.string.msg_permissions_enabling)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun requestOverlayPermission() {
        showToastMessage(getString(R.string.msg_permit_drawing))
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName"))
        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
        //wait for onActivityResult()
    }


    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            REQUEST_PERMISSION_SETTING -> {
                Timber.d("onActivityResult REQUEST_PERMISSION_SETTING")
                checkPermissions()
            }
            OVERLAY_PERMISSION_REQ_CODE -> {
                val overlayPermissionGranted = Settings.canDrawOverlays(this)
                Timber.d("Granted overlay: %s", overlayPermissionGranted)
                checkPermissions()
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun checkPermissions() {
        val helper = PermissionsUtils(this, TasBoilerplateSettings.NECESSARY_PERMISSIONS)
        val grantedPermissions = helper.getGrantedPermissions()
        val size = grantedPermissions.size
        Timber.d("Size: %s", size)
        if (size == TasBoilerplateSettings.NECESSARY_PERMISSIONS.size) {
            goAheadAfterPermissionsEnabling()
        } else if (isOnlyOverlayPermNeeded(helper.getNeededPermissions())) {
            requestOverlayPermission()
        } else {
            openSettings()
        }
    }

    companion object {
        val SUPPRESS_PERMISSIONS = "suppress_permissions"
        private val OVERLAY_PERMISSION_REQ_CODE = 5345
        private val REQUEST_PERMISSION_SETTING = 5346
        private val ASK_PERMISSION_REQ_CODE = 5347
    }
}
