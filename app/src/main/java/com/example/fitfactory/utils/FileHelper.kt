package com.example.fitfactory.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

object FileHelper {

    fun getFileAbsolutePath(context: Context?, data: Uri): String?{
        val filePath = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context?.contentResolver?.query(data, filePath, null, null, null)
        cursor?.let {
            val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(index)
            cursor.close()
            return path
        }
        return null

    }
}