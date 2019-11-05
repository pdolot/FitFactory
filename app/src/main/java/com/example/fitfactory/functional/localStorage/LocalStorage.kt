package com.example.fitfactory.functional.localStorage

import com.example.fitfactory.data.models.User

interface LocalStorage {
    fun isLogged(): Boolean
    fun saveUser(user: User)
    fun getUser(): User?
    fun signOut()
    fun getAccountType(): AccountType?
}

enum class AccountType{
    FACEBOOK,
    GOOGLE,
    FITFACTORY
}