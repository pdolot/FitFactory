package com.example.fitfactory.functional.localStorage

import android.content.Context
import com.example.fitfactory.data.models.SharedPrefUser
import com.example.fitfactory.data.models.UserGetResource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPrefLocalStorage(context: Context) : LocalStorage {

    private var sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val liveSharedPreferences = LiveSharedPreferences(sharedPref)

    override fun saveFacebookAccount(username: String, password: String, email: String) {
        sharedPref.edit().apply {
            putString(FF_FACEBOOK_ACCOUNT, addFacebookUser(username, password, email))
            apply()
        }
    }

    override fun getFacebookAccount(): SharedPrefUser? {
        return getFacebookUser()
    }

    override fun saveGoogleAccount(username: String, password: String, email: String) {
        sharedPref.edit().apply {
            putString(FF_GOOGLE_ACCOUNTS, addGoogleUser(username, password, email))
            apply()
        }
    }

    override fun getGoogleAccount(email: String): SharedPrefUser? {
        return getGoogleAccounts()?.find {user -> user.email == email }
    }

    override fun isLoggedLive(): LivePreference<Boolean> {
        return liveSharedPreferences.getBoolean(IS_LOGGED, false)
    }

    override fun isLogged(): Boolean {
        return sharedPref.getBoolean(IS_LOGGED, false)
    }

    override fun readToken(): String? {
        return sharedPref.getString(IS_LOGGED, null)
    }

    override fun setToken(token: String?) {
        sharedPref.edit().apply {
            putBoolean(IS_LOGGED, token != null)
            putString(TOKEN, token)
            apply()
        }
    }

    override fun setUser(user: UserGetResource) {
        sharedPref.edit().apply {
            putString(USER, Gson().toJson(user))
            apply()
        }
    }

    override fun getUser(): UserGetResource? {
        val user = sharedPref.getString(USER, null) ?: return null
        val type = object : TypeToken<UserGetResource>() {}.type
        return Gson().fromJson(user, type)
    }


    private fun addFacebookUser(username: String, password: String, email: String): String {
        return Gson().toJson(SharedPrefUser(username, password, email))
    }

    private fun getFacebookUser(): SharedPrefUser? {
        val user = sharedPref.getString(FF_FACEBOOK_ACCOUNT, null)
        val type = object : TypeToken<SharedPrefUser>() {}.type
        return Gson().fromJson(user, type)
    }

    private fun addGoogleUser(username: String, password: String, email: String): String {
        val users = getGoogleAccounts() ?: ArrayList()
        users.add(SharedPrefUser(username, password, email))
        return Gson().toJson(users)
    }

    private fun getGoogleAccounts(): ArrayList<SharedPrefUser>? {
        val users = sharedPref.getString(FF_GOOGLE_ACCOUNTS, null) ?: return null
        val type = object : TypeToken<ArrayList<SharedPrefUser>>() {}.type
        return Gson().fromJson(users, type) as ArrayList<SharedPrefUser>
    }

    companion object {
        const val PREFS_NAME = "fitfactory"
        const val IS_LOGGED = "isLoggedLive"
        const val TOKEN = "token"
        const val USER = "user"
        const val FF_GOOGLE_ACCOUNTS = "fitFactoryGoogleAccounts"
        const val FF_FACEBOOK_ACCOUNT = "fitFactoryFacebookAccounts"
    }

}