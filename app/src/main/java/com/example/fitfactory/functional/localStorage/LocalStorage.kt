package com.example.fitfactory.functional.localStorage

import com.example.fitfactory.data.models.SharedPrefUser
import com.example.fitfactory.data.models.UserGetResource

interface LocalStorage {
    fun isLoggedLive(): LivePreference<Boolean>
    fun isLogged(): Boolean
    fun setToken(token: String?)
    fun readToken(): String?
    fun setUser(user: UserGetResource)
    fun getUser(): UserGetResource?
    fun saveFacebookAccount(username: String, password: String, email: String)
    fun getFacebookAccount(): SharedPrefUser?
    fun saveGoogleAccount(username: String, password: String, email: String)
    fun getGoogleAccount(email: String): SharedPrefUser?
}