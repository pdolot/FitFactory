package com.example.fitfactory.presentation.fragments.signIn

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfactory.constants.RequestCode
import com.example.fitfactory.di.Injector
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import javax.inject.Inject
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.fitfactory.data.models.request.SignInRequest
import com.example.fitfactory.data.models.request.SignUpRequest
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.customViews.SignInDialog
import com.example.fitfactory.utils.Validator
import com.facebook.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.concurrent.TimeUnit


class SignInViewModel : ViewModel() {
    @Inject
    lateinit var googleClient: GoogleSignInClient

    // Facebook
    private var callbackManager = CallbackManager.Factory.create()

    @Inject
    lateinit var localStorage: LocalStorage

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    @Inject
    lateinit var activity: AppCompatActivity

    var callResult = MutableLiveData<Any>()

    init {
        Injector.component.inject(this)
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {

                val request = GraphRequest.newMeRequest(
                    result?.accessToken
                ) { json, response ->
                    val email = json.getString("email")
                    val birthday = json.getString("birthday")
                    val profile = Profile.getCurrentProfile()
                    localStorage.getFacebookAccount()?.let {
                        signIn(it.username, it.password)
                    } ?: run {
                        val view = SignInDialog(activity)
                        MaterialDialog(activity).show {
                            title(text = "Pierwsze logowanie")
                            message(text = "Podaj nazwę użytkownika i hasło. Tych danych będziesz mógł użyć do zwykłego logowania")
                            customView(view = view)
                            positiveButton(text = "ZAPISZ"){
                                val user = SignUpRequest(username = view.getUsername(),
                                    password = view.getPassword(),
                                    email = email,
                                    firstName = profile.firstName,
                                    lastName = profile.lastName,
                                    birthDate = birthday,
                                    profileImage = profile.getProfilePictureUri(300, 300).toString())
                                signUp(user)
                            }
                            negativeButton(text = "ANULUJ"){
                                callResult.postValue(CancelSignIn())
                                dismiss()
                            }
                        }
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender,birthday")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                callResult.postValue(CancelSignIn())
            }

            override fun onError(error: FacebookException?) {
                callResult.postValue(ErrorSignIn("Błąd pobierania konta Facebook"))
            }

        })
    }

    fun facebookSignIn(owner: Fragment) {
        LoginManager.getInstance().logInWithReadPermissions(owner, mutableListOf("public_profile","email", "user_birthday"))
    }

    fun googleSignIn(owner: Fragment) {
        googleClient.signOut()
        owner.startActivityForResult(
            googleClient.signInIntent,
            RequestCode.GOOGLE_SIGN_IN_REQUEST_CODE
        )
    }

    fun defaultSignIn(username: String, password: String){
        signIn(username, password)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.GOOGLE_SIGN_IN_REQUEST_CODE -> {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }

    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {

            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            account?.let {
                it.email?.let {email ->
                    localStorage.getGoogleAccount(email)?.apply {
                        signIn(username, password)
                    } ?: run {
                        val view = SignInDialog(activity)
                        MaterialDialog(activity).show {
                            title(text = "Pierwsze logowanie")
                            message(text = "Podaj nazwę użytkownika i hasło. Tych danych będziesz mógł użyć do zwykłego logowania")
                            customView(view = view)
                            positiveButton(text = "ZAPISZ"){
                                val user = SignUpRequest(username = view.getUsername(),
                                    password = view.getPassword(),
                                    email = email,
                                    firstName = account.givenName,
                                    lastName = account.familyName)
                                signUp(user)
                            }
                            negativeButton(text = "ANULUJ"){
                                callResult.postValue(CancelSignIn())
                                dismiss()
                            }
                        }

                    }
                }

            } ?: run {
                callResult.postValue(ErrorSignIn("Brak danych konta"))
            }


        } catch (e: ApiException) {
            callResult.postValue(ErrorSignIn("Błąd pobierania konta Google"))
        }
    }

    @SuppressLint("CheckResult")
    private fun signUp(user: SignUpRequest){
        retrofitRepository.signUp(user)
            .subscribeOn(Schedulers.io())
            .delay(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    if (it.status){
                        localStorage.saveGoogleAccount(user.username, user.password, user.email)
                        signIn(user.username, user.password)
                    }else{
                        callResult.postValue(ErrorSignIn(it.message))
                    }
                },
                onError = {
                    callResult.postValue(ErrorSignIn(it.message))
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun signIn(username: String, password: String){
        retrofitRepository.signIn(SignInRequest(username, password))
            .subscribeOn(Schedulers.io())
            .delay(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    localStorage.setToken("Bearer ${it.token}")
                    localStorage.setUser(it.user)
                    callResult.postValue(SuccessSignIn())
                },
                onError = {
                    callResult.postValue(ErrorSignIn(it.localizedMessage))
                }
            )
    }

    fun validate(username: EditText, password: EditText): Boolean{
        if (!Validator(username).validateTextField()) return false
        if (!Validator(password).validatePassword()) return false
        return true
    }
}

class SuccessSignIn
class CancelSignIn
class ErrorSignIn(val message: String?)
