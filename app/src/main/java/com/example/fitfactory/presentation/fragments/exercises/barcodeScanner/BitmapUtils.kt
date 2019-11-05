package com.example.fitfactory.presentation.fragments.exercises.barcodeScanner

import android.graphics.*
import android.hardware.Camera
import android.util.Log
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.nio.ByteBuffer

/** Utils functions for bitmap conversions. */
@Suppress("DEPRECATION")
class BitmapUtils {
    companion object{
        // Convert NV21 format byte buffer to bitmap.
        fun getBitmap(data: ByteBuffer, metadata: FrameMetadata): Bitmap?{
            data.rewind()
            val imageInBuffer = ByteArray(data.limit())
            data.get(imageInBuffer, 0, imageInBuffer.size)
            try {
                val image: YuvImage? = YuvImage(imageInBuffer, ImageFormat.NV21, metadata.width, metadata.height, null)
                image?.let {
                    val stream = ByteArrayOutputStream()
                    it.compressToJpeg(Rect(0,0,metadata.width, metadata.height),80, stream)
                    val bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size())

                    stream.close()
                    return rotateBitmap(bmp, metadata.rotation, metadata.cameraFacing)
                }
            }catch (e: Exception){
                Log.e("VisionProcessorBase","Error: ${e.message}")
            }
            return null
        }

        // Rotates a bitmap if it is converted from a bytebuffer.
        private fun rotateBitmap(bmp: Bitmap, rotation: Int, cameraFacing: Int): Bitmap {
            val matrix = Matrix()
            val rotationDegree = when(rotation){
                FirebaseVisionImageMetadata.ROTATION_90 -> 90f
                FirebaseVisionImageMetadata.ROTATION_180 -> 180f
                FirebaseVisionImageMetadata.ROTATION_270 -> 270f
                else -> 0f
            }
            // Rotate the image back to straight.
            matrix.postRotate(rotationDegree)
            return if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            } else {
                // Mirror the image along X axis for front-facing camera image.
                matrix.postScale(-1.0f, 1.0f)
                Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            }
        }


    }
}