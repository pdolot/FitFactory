package com.example.fitfactory.presentation.pages.exercises.scannerView

import android.annotation.SuppressLint
import android.app.Activity
import android.hardware.Camera.Parameters.FLASH_MODE_OFF
import android.hardware.Camera.Parameters.FLASH_MODE_TORCH
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitfactory.presentation.pages.exercises.barcodeScanner.BarcodeAnalyzer
import com.example.fitfactory.presentation.pages.exercises.barcodeScanner.CameraSource
import com.example.fitfactory.presentation.pages.exercises.barcodeScanner.CameraSourcePreview
import com.example.fitfactory.presentation.pages.exercises.barcodeScanner.GraphicOverlay

class ScannerViewModel : ViewModel() {
    val code = MutableLiveData<Any>()
    val barcode = MutableLiveData<String>()
    var cameraSource: CameraSource? = null
    var isTorchEnabled = false
        set(value) {
            field = value
            setTorch()
        }

    private val analyzer = BarcodeAnalyzer({
        it.firstOrNull()?.rawValue?.let {
//            SharedPrefLocalStorage(context).saveCard(it)
            barcode.postValue(it)
        }
    }, {
        code.postValue(it)
    })

    @SuppressLint("MissingPermission")
    fun startCamera(
        activity: Activity,
        firePreview: CameraSourcePreview,
        fireFaceOverlay: GraphicOverlay
    ) {
        cameraSource = CameraSource(activity, fireFaceOverlay)
        cameraSource?.apply {
            setMachineLearningFrameProcessor(analyzer)
            firePreview.start(this)
        }
    }

    @Suppress("DEPRECATION")
    fun setTorch() {
        cameraSource?.setFlashMode(if (isTorchEnabled) FLASH_MODE_TORCH else FLASH_MODE_OFF)
    }
}
