package com.example.fitfactory.functional.localStorage

import com.example.fitfactory.data.models.User

interface LocalStorage {
    fun isLoggedLive(): LivePreference<Boolean>
    fun isLogged(): Boolean
    fun setToken(token: String?)
    fun readToken(): String?
}