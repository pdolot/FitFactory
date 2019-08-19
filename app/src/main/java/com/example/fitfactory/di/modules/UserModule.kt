package com.example.fitfactory.di.modules

import com.example.fitfactory.data.models.User
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserModule{

    @Singleton
    @Provides
    fun provideUser() = User()
}