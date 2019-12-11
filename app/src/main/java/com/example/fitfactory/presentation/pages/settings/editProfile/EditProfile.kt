package com.example.fitfactory.presentation.pages.settings.editProfile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.fitfactory.R
import com.example.fitfactory.constants.RequestCode
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.utils.FileHelper
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
        bindData()
        editProfileImage.setOnClickListener {
            EditProfileImageBottomSheet.newInstance().apply {
                onGallerySelect = {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequestCode.READ_EXTERNAL_STORAGE_REQUEST_CODE)
                }
                onCameraSelect = {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), RequestCode.CAMERA_REQUEST_CODE)
                }
            }.show(fm)
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

            Glide.with(context ?: return)
                .load(it.profileImage)
                .placeholder(R.drawable.user_image)
                .fitCenter()
                .into(profileImage)
        }
    }

    private fun pickImageFromGallery(){
        startActivityForResult(
            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                val mimeTypes = arrayListOf("image/jpeg","image/png")
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            },
            RequestCode.GALLERY_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
            RequestCode.READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                RequestCode.GALLERY_REQUEST_CODE -> {
                    data?.data?.let {
                        val path = FileHelper.getFileAbsolutePath(context, it)
                        path?.let { viewModel.putFileToFirebase(path) }
                        Glide.with(context ?: return)
                            .load(it)
                            .placeholder(R.drawable.user_image)
                            .fitCenter()
                            .into(profileImage)
                    }

                }
            }
        }
    }

}
