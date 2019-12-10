package com.example.fitfactory.presentation.pages.settings.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment

class EditProfile : BaseFragment() {


    private val viewModel by lazy { EditProfileViewModel() }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.editProfile)
    override fun topBarEnabled() = true
    override fun backButtonEnabled() = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }


}
