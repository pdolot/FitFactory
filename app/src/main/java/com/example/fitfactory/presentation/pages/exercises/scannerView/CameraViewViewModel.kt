package com.example.fitfactory.presentation.pages.exercises.scannerView

import android.content.Context
import android.graphics.Rect
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.fitfactory.R
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.base.BaseViewModel
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class CameraViewViewModel : BaseViewModel(), FrameProcessor {

    @Inject
    lateinit var context: Context

    init {
        Injector.component.inject(this)
    }

    private var metaData: FirebaseVisionImageMetadata.Builder? = null
    private var detector: FirebaseVisionBarcodeDetector
    var cameraFacing: Facing = Facing.BACK
    var frameRect: Rect? = null
    val result = MutableLiveData<String>()
    var isAnalyzingEnabled = true

    var checkStatusResult = MutableLiveData<Any>()
    var verifyResult = MutableLiveData<Any>()
    var addEntryResult = MutableLiveData<Any>()

    init {
        metaData = FirebaseVisionImageMetadata.Builder()
            .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
            .setRotation(if (cameraFacing == Facing.FRONT) FirebaseVisionImageMetadata.ROTATION_270 else FirebaseVisionImageMetadata.ROTATION_90)

        var options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
            .build()

        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
    }

    @WorkerThread
    override fun process(frame: Frame) {

        frameRect?.let {
            val image = metaData?.setWidth(frame.size.width)?.setHeight(frame.size.height)
                ?.setRotation(if (cameraFacing == Facing.FRONT) FirebaseVisionImageMetadata.ROTATION_270 else FirebaseVisionImageMetadata.ROTATION_90)?.let {
                    FirebaseVisionImage.fromByteArray(frame.getData(), it.build())
                } ?: return

            detector.detectInImage(image)
                .addOnSuccessListener { list ->
                    if (list.isNotEmpty() && isAnalyzingEnabled) {
                        list.firstOrNull()?.let {
                            if (checkIfBarCodeIsInFrame(it.boundingBox)) {
                                isAnalyzingEnabled = false
                                result.postValue(it.rawValue)
                            }
                        }
                    }
                }
        }
    }

    private fun checkIfBarCodeIsInFrame(barCodeRect: Rect?): Boolean {
        barCodeRect?.apply {
            top += context.resources.getDimensionPixelSize(R.dimen.topBarHeight)
            bottom += context.resources.getDimensionPixelSize(R.dimen.topBarHeight)
        }
        frameRect?.also { frameRect ->
            barCodeRect?.also { b ->
                if (frameRect.left < b.left && frameRect.top < b.top && frameRect.right > b.right && frameRect.bottom > b.bottom) {
                    val vProp =
                        (b.bottom - b.top).toFloat() / (frameRect.bottom - frameRect.top).toFloat()
                    val hProp =
                        (b.right - b.left).toFloat() / (frameRect.right - frameRect.left).toFloat()

                    if (vProp >= 0.7f && hProp >= 0.7f) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
