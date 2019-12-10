package com.example.fitfactory.presentation.pages.settings.editProfile

import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.base.BaseViewModel
import javax.inject.Inject

class EditProfileViewModel : BaseViewModel() {
    @Inject
    lateinit var localStorage: LocalStorage

    init {
        Injector.component.inject(this)
    }
}
