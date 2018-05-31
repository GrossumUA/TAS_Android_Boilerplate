package com.theappsolutions.boilerplate.util.other.permissions_check

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.util.SparseArray

import java.util.ArrayList
import java.util.Arrays

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
class PermissionsUtils {

    private val mContext: Context
    private val necessaryPermissions: ArrayList<String>
    private val grantedPermissions: ArrayList<String>

    constructor(mContext: Context, necessaryPermissions: Array<String>) {
        this.mContext = mContext
        this.necessaryPermissions = ArrayList<String>()
        this.grantedPermissions = ArrayList<String>()
        this.necessaryPermissions.addAll(necessaryPermissions)
    }

    fun getNeededPermissions(): List<String> {
        val neededPermissions = ArrayList<String>()
        for (permission in necessaryPermissions) {
            if (ContextCompat.checkSelfPermission(mContext,
                            permission) == PackageManager.PERMISSION_DENIED) {

                if (permission == Manifest.permission
                                .SYSTEM_ALERT_WINDOW) {
                    if (!checkOverlay()) {
                        neededPermissions.add(permission)
                    }
                } else {
                    neededPermissions.add(permission)
                }

            }
        }
        return neededPermissions
    }

    @TargetApi(23)
    private fun checkOverlay(): Boolean {
        return Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(mContext)
    }

    fun getGrantedPermissions(): List<String> {

        val mGrantedPermissions = ArrayList<String>()
        for (permission in necessaryPermissions) {
            if (permission == Manifest.permission.SYSTEM_ALERT_WINDOW && checkOverlay()) {
                mGrantedPermissions.add(Manifest.permission
                        .SYSTEM_ALERT_WINDOW)
            } else if (ContextCompat.checkSelfPermission(mContext,
                            permission) == PackageManager.PERMISSION_GRANTED) {
                mGrantedPermissions.add(permission)
            }
        }
        return mGrantedPermissions
    }

    @Synchronized
    private fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray) {
        for (i in grantResults.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permissions[i])
            }
        }
    }

    companion object {
        private val INSTANCES = SparseArray<PermissionsUtils>()
        private var sNextRequestCode: Int = 0

        @Synchronized
        private fun getInstance(code: Int): PermissionsUtils {
            val instance = INSTANCES.get(code)
            INSTANCES.remove(code)
            return instance
        }

        @Synchronized
        private fun saveInstance(instance: PermissionsUtils): Int {
            val code = sNextRequestCode++
            INSTANCES.put(code, instance)
            return code
        }

        internal fun onRequestPermissionsResult(
                permissions: Array<String>, grantResults: IntArray, requestCode: Int) {
            getInstance(requestCode).onRequestPermissionsResult(permissions, grantResults)
        }

        fun openSettings(activity: Activity) {
            val i = Intent()
            i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.data = Uri.parse("package:" + activity.packageName)
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            activity.startActivity(i)
        }
    }

}
