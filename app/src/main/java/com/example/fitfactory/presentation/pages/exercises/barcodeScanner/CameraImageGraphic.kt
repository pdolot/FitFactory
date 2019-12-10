package com.example.fitfactory.presentation.pages.exercises.barcodeScanner

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

class CameraImageGraphic(overlay: GraphicOverlay, private val bitmap: Bitmap) : GraphicOverlay.Graphic(overlay) {

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, null, Rect(0,0, canvas.width, canvas.height), null)
    }
}