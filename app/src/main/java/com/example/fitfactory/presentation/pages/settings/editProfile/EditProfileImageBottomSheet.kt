package com.example.fitfactory.presentation.pages.settings.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseBottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_edit_profile_image.*

class EditProfileImageBottomSheet : BaseBottomSheet() {

    var onGallerySelect: () -> Unit = {}
    var onCameraSelect: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_edit_profile_image, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fromCamera.setOnClickListener {
            onCameraSelect()
            dismiss()
        }

        fromGallery.setOnClickListener {
            onGallerySelect()
            dismiss()
        }
    }

    companion object{
        fun newInstance() = EditProfileImageBottomSheet()
    }

}