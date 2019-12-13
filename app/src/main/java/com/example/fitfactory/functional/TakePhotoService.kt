package com.example.fitfactory.functional

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.fitfactory.constants.PhotoServiceRequestCode
import com.example.fitfactory.constants.RequestCode
import com.example.fitfactory.utils.FileHelper
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.io.File
import java.util.*

class TakePhotoService(private val owner: Fragment) {

    var photoServiceResult: (Uri?) -> Unit = {}
    private var photoUri: Uri? = null
    private var fileName: String? = null

    fun startCameraIntent() {
        val context = owner.context ?: return
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context.packageManager)?.also {
                createImageFile(context)?.also { file ->
                    photoUri = FileProvider.getUriForFile(
                        context,
                        PhotoServiceRequestCode.FILE_PROVIDER,
                        file
                    )
                    fileName = file.absolutePath
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    owner.startActivityForResult(
                        takePictureIntent,
                        PhotoServiceRequestCode.REQUEST_TAKE_PHOTO
                    )
                }
            }
        }
    }

    fun startGalleryIntent(){
        owner.startActivityForResult(
            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                val mimeTypes = arrayListOf("image/jpeg", "image/png")
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            },
            RequestCode.GALLERY_REQUEST_CODE
        )
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                PhotoServiceRequestCode.REQUEST_TAKE_PHOTO -> {
                    photoServiceResult(photoUri)
                }
                RequestCode.GALLERY_REQUEST_CODE -> {
                    data?.data?.let {
                        val path = FileHelper.getFileAbsolutePath(owner.context, it) ?: return
                        photoServiceResult(Uri.parse(path))

                    }
                }
            }
        }
    }

    fun removePhoto(){
        fileName?.let {
            val file = File(it)
            if (file.exists()){
                file.delete()
                photoServiceResult(null)
            }
        }
    }

    private fun createImageFile(context: Context): File? {
        val folder = File(context.filesDir, "photos")
        folder.mkdirs()
        return File(folder, generatePhotoFileName() ?: return null)
    }

    private fun generatePhotoFileName(): String? {
        val uid = UUID.randomUUID().toString()
        val timeStamp =
            (DateTimeFormat.forPattern("dd_MM_yyyy_HHmmsss") as DateTimeFormatter).print(
                DateTime.now()
            )
        return timeStamp + "_" + uid + ".jpg"
    }
}