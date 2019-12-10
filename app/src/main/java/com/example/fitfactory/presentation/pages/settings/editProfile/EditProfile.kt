package com.example.fitfactory.presentation.pages.settings.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit_profile.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
    }

    private fun bindData() {
        val user = viewModel.localStorage.getUser()
        user?.let {
            firstName.setText(user.firstName)
            secondName.setText(user.secondName)
            lastName.setText(user.lastName)
            email.setText(user.email)
            personalIdentity.setText(user.identityNumber)
            userBirthDate.setText(user.birthDate)
            userPhoneNo.setText(user.phoneNumber)
            userAddressStreet.setText(user.address?.street)
            userAddressCity.setText(user.address?.city)
            userZipCode.setText(user.address?.zipCode)
            userZipCodeCity.setText(user.address?.zipCodeCity)
        }
    }


}
