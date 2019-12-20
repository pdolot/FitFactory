package com.example.fitfactory.presentation.pages.settings.editProfile

import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.getCustomView
import com.example.fitfactory.constants.RequestCode
import com.example.fitfactory.data.database.creditCard.CreditCardDao
import com.example.fitfactory.data.models.app.*
import com.example.fitfactory.data.models.request.ChangePasswordRequest
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.TakePhotoService
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.base.BaseViewModel
import com.example.fitfactory.presentation.customViews.changePasswordDialog.ChangePasswordDialog
import com.google.firebase.storage.StorageReference
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

class EditProfileViewModel : BaseViewModel() {
    @Inject
    lateinit var localStorage: LocalStorage

    @Inject
    lateinit var storageRef: StorageReference

    @Inject
    lateinit var activity: AppCompatActivity

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var creditCardDao: CreditCardDao

    private lateinit var owner: Fragment

    lateinit var takePhotoService: TakePhotoService

    var photoServiceResult = MutableLiveData<String?>()

    var updateState = MutableLiveData<Any>()

    var user: UserGetResource? = null

    var creditCard: CreditCard? = null

    init {
        Injector.component.inject(this)
        user = localStorage.getUser()
    }

    var passwordChanged = MutableLiveData<Boolean>()

    fun setOwner(owner: Fragment) {
        this.owner = owner
        takePhotoService = TakePhotoService(owner)
        takePhotoService.photoServiceResult = {
            photoServiceResult.postValue(it)
        }
    }

    fun editUserProfile() {
        user?.let { user ->
            updateState.postValue(StateInProgress())
            photoServiceResult.value?.let {
                putFileToFirebase(it, user)
            } ?: updateUser(user)
        }

    }

    private fun putFileToFirebase(path: String, user: UserGetResource) {
        var file = Uri.fromFile(File(path))
        val userId = localStorage.getUser()?.id ?: UUID.randomUUID()
        val ref = storageRef.child("images/$userId/${file.lastPathSegment}")
        ref.putFile(file)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    updateState.postValue(StateError("Błąd połączenia z serwerem"))
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.profileImage = task.result?.toString()
                    updateUser(user)
                } else {
                    updateState.postValue(StateError("Błąd wysyłania zdjęcia"))
                }
            }
    }

    private fun updateUser(user: UserGetResource) {
        rxDisposer.add(retrofitRepository.editUser(user)
            .subscribeBy(
                onSuccess = {
                    if (it.status) {
                        localStorage.setUser(it.data)
                        updateState.postValue(StateComplete())
                    } else {
                        updateState.postValue(StateError(it.message))
                    }
                },
                onError = {
                    updateState.postValue(StateError("Błąd połączenia z serwerem"))
                }
            ))
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

    fun changePassword(passwords: Pair<String, String>, dialog: MaterialDialog) {
        rxDisposer.add(
            retrofitRepository.changePassword(
                ChangePasswordRequest(
                    localStorage.getUser()?.username ?: return, passwords.first, passwords.second
                )
            )
                .subscribeBy(
                    onSuccess = {
                        if (it.status) {
                            dialog.dismiss()
                            passwordChanged.postValue(true)
                        } else {
                            (dialog.getCustomView() as ChangePasswordDialog).setErrorMessage(it.message)
                        }
                    },
                    onError = {
                        Log.e("EditProfile", it.message)
                        (dialog.getCustomView() as ChangePasswordDialog).setErrorMessage("Brak połączenia z serwerem. Spróbuj ponownie później")
                    }
                )
        )
    }

    fun addCreditCard(creditCard: CreditCard) = viewModelScope.launch {
        creditCard.ownerId = user?.id ?: return@launch
        creditCardDao.insert(creditCard)
    }

    fun deleteCreditCard() = viewModelScope.launch {
        creditCardDao.deleteCreditCard(user?.id ?: return@launch)
    }
}
