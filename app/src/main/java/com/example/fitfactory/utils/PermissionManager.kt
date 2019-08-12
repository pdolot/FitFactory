package com.example.fitfactory.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager(private val activity: AppCompatActivity) {
    
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

    fun checkPermission(permission: String): Boolean{
        return (ContextCompat.checkSelfPermission(activity.applicationContext, permission)
                == PackageManager.PERMISSION_GRANTED)
    }
}