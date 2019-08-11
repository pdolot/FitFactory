package com.example.fitfactory.utils

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionRequestManager(private val activity: AppCompatActivity) {
    
    fun sendRequest(permission: String) {
        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), Constants.LOCATION_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), Constants.LOCATION_REQUEST_CODE)
            }
        }
    }
}