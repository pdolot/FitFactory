package com.example.fitfactory.functional.localStorage

import android.content.Context
import com.example.fitfactory.data.models.User
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn

class SharedPrefLocalStorage(private val context: Context) : LocalStorage {
    override fun getAccountType(): AccountType? {
        AccessToken.getCurrentAccessToken()?.let {
            if (!it.isExpired) {
                return AccountType.FACEBOOK
            }
        }

        GoogleSignIn.getLastSignedInAccount(context)?.let {
            return AccountType.GOOGLE
        }

        return null
    }

    override fun isLogged(): Boolean {
        return getAccountType() != null
    }

    override fun saveUser(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUser(): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signOut() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    companion object {
        const val PREFS_NAME = "fitfactory"
    }

}