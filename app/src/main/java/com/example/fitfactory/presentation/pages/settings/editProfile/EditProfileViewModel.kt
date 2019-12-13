package com.example.fitfactory.presentation.pages.settings.editProfile

import android.content.pm.PackageManager
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.fitfactory.constants.RequestCode
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.TakePhotoService
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.base.BaseViewModel
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.*
import javax.inject.Inject

class EditProfileViewModel : BaseViewModel() {
    @Inject
    lateinit var localStorage: LocalStorage

    @Inject
    lateinit var storageRef: StorageReference

    private lateinit var owner: Fragment

    lateinit var takePhotoService: TakePhotoService

    var photoServiceResult = MutableLiveData<String?>()

    init {
        Injector.component.inject(this)
    }

    fun setOwner(owner: Fragment){
        this.owner = owner
        takePhotoService = TakePhotoService(owner)
        takePhotoService.photoServiceResult = {
            photoServiceResult.postValue(it?.toString())
        }
    }

    fun putFileToFirebase(path: String) {
        var file = Uri.fromFile(File(path))
        val userId = localStorage.getUser()?.id ?: UUID.randomUUID()
        val ref = storageRef.child("images/$userId/${file.lastPathSegment}")
        ref.putFile(file)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    println(downloadUri)
                } else {
                    // Handle failures
                    // ...
                }
            }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RequestCode.READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoService.startGalleryIntent()
                }
            }
            RequestCode.CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoService.startCameraIntent()
                }
            }
        }
    }

}
