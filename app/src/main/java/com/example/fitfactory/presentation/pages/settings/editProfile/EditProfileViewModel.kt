package com.example.fitfactory.presentation.pages.settings.editProfile

import android.net.Uri
import com.example.fitfactory.di.Injector
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

    init {
        Injector.component.inject(this)
    }

    fun putFileToFirebase(path: String){
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
}
