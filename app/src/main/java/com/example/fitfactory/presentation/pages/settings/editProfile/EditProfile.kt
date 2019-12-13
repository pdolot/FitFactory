package com.example.fitfactory.presentation.pages.settings.editProfile

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.fitfactory.R
import com.example.fitfactory.constants.RequestCode
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import javax.inject.Inject

class EditProfile : BaseFragment() {

    @Inject
    lateinit var fm: FragmentManager

    init {
        Injector.component.inject(this)
    }

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
        viewModel.setOwner(this)
        bindData()
        editProfileImage.setOnClickListener {
            EditProfileImageBottomSheet.newInstance().apply {
                onGallerySelect = ::checkReadExternalStoragePermission
                onCameraSelect = ::checkCameraPermission
            }.show(fm)
        }

        viewModel.photoServiceResult.observe(viewLifecycleOwner, Observer {
            deleteImage.visibility = if (it != null) View.VISIBLE else View.GONE

            setProfileImage(it ?: viewModel.localStorage.getUser()?.profileImage)
        })

        deleteImage.setOnClickListener {
            viewModel.takePhotoService.removePhoto()
        }
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
            setProfileImage(it.profileImage)
        }
    }

    private fun checkReadExternalStoragePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            RequestCode.READ_EXTERNAL_STORAGE_REQUEST_CODE
        )
    }

    private fun checkCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            RequestCode.CAMERA_REQUEST_CODE
        )
    }

    private fun setProfileImage(source: String?) {
        Glide.with(context ?: return)
            .load(source)
            .placeholder(R.drawable.user_image)
            .fitCenter()
            .into(profileImage)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        viewModel.onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.takePhotoService.onActivityResult(requestCode, resultCode, data)
    }

}
