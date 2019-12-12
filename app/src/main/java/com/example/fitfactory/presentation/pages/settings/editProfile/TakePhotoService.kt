package com.example.fitfactory.presentation.pages.settings.editProfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.fitfactory.constants.PhotoServiceRequestCode
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.io.File
import java.util.*

class TakePhotoService(private val owner: Fragment) {

    var photoServiceResult: (Uri) -> Unit = {}
    private lateinit var photoUri: Uri

    fun startCameraIntent() {

        val context = owner.context ?: return
        val activity = owner.activity ?: return

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context.packageManager)?.also {
                createImageFile(context).also { file ->
                    photoUri = FileProvider.getUriForFile(
                        context,
                        PhotoServiceRequestCode.FILE_PROVIDER,
                        file
                    )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    activity.startActivityForResult(
                        takePictureIntent,
                        PhotoServiceRequestCode.REQUEST_TAKE_PHOTO
                    )
                }
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != PhotoServiceRequestCode.REQUEST_TAKE_PHOTO) return

        when (resultCode) {
            Activity.RESULT_OK -> photoServiceResult(photoUri)
        }
    }

    private fun createImageFile(context: Context): File {
        val folder = File(context.filesDir, "photos")
        folder.mkdirs()
        return File(folder, generatePhotoFileName())
    }

    private fun generatePhotoFileName(): String {
        val uid = UUID.randomUUID().toString()
        val timeStamp =
            (DateTimeFormat.forPattern("dd_MM_yyyy_HHmmsss") as DateTimeFormatter).print(
                DateTime.now()
            )
        return timeStamp + "_" + uid
    }
}