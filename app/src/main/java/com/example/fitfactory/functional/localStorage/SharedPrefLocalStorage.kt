package com.example.fitfactory.functional.localStorage

import android.content.Context

class SharedPrefLocalStorage(context: Context) : LocalStorage {

    private var sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val liveSharedPreferences = LiveSharedPreferences(sharedPref)

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

    companion object {
        const val PREFS_NAME = "fitfactory"
        const val IS_LOGGED = "isLoggedLive"
        const val TOKEN = "token"
    }

}