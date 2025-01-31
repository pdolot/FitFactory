package com.example.fitfactory.presentation.pages.exercises.info

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.fitfactory.R
import com.example.fitfactory.constants.RequestCode.Companion.CAMERA_REQUEST_CODE
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.utils.SpanTextUtil
import kotlinx.android.synthetic.main.fragment_exercises_info.*

class ExercisesInfoFragment : BaseFragment() {


    private val viewModel by lazy { ExercisesInfoViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercises_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SpanTextUtil(context ?: return).apply {
            setSpanOnTextView(firstInfo, "QR", R.color.primaryLight)
            setSpanOnTextView(secondInfo, "kontuzji", R.color.negativeMedium)
        }

        scanButton.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                grantResults.any {
                    if (it == PackageManager.PERMISSION_GRANTED) {
                        findNavController().navigate(R.id.scanner)
                        true
                    }
                    false
                }
            }
        }
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.exercises)
    override fun topBarEnabled() = true
    override fun backButtonEnabled(): Boolean = false
}
