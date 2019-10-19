package com.example.fitfactory.presentation.fragments.exercises.barcodeScanner

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class BarcodeAnalyzer(
    private val onDetected: (qrCodes: List<FirebaseVisionBarcode>) -> Unit,
    private val onError: (Exception) -> Unit
) : VisionProcessorBase<List<FirebaseVisionBarcode>>() {
    var options = FirebaseVisionBarcodeDetectorOptions.Builder()
        .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
        .build()

    private val detector by lazy { FirebaseVision.getInstance().getVisionBarcodeDetector(options) }

    override fun detectInImage(image: FirebaseVisionImage): Task<List<FirebaseVisionBarcode>> {
        return detector.detectInImage(image)
    }

    override fun onSuccess(
        originalCameraImage: Bitmap?,
        results: List<FirebaseVisionBarcode>,
        frameMetadata: FrameMetadata,
        graphicOverlay: GraphicOverlay
    ) {
        graphicOverlay.clear()
        originalCameraImage?.let {
            val imageGraphic = CameraImageGraphic(graphicOverlay, it)
            graphicOverlay.add(imageGraphic)
        }

        graphicOverlay.postInvalidate()

        onDetected(results)
    }

    override fun onFailure(e: Exception) {
        onError(e)
    }

}