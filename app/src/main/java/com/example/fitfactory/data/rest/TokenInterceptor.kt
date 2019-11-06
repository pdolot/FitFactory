package com.example.fitfactory.data.rest


import com.example.fitfactory.constants.RestConstants.Companion.BEARER
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor : Interceptor {
    @Inject
    lateinit var localStorage: LocalStorage

    init {
        Injector.component.inject(this)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
        if (localStorage.isLogged()) {
            localStorage.readToken()?.let {
                newRequest.addHeader("Authorization", "$BEARER $it")
            }
        }
        return chain.proceed(newRequest.build())
    }
}