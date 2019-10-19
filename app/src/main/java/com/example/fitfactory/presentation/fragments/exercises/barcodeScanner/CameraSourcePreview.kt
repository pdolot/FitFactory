package com.example.fitfactory.presentation.fragments.exercises.barcodeScanner


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

class CameraSourcePreview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    //    private var surfaceView: SurfaceView = SurfaceView(context)
    private var startRequested: Boolean = false
    private var surfaceAvailable: Boolean = false
    private var cameraSource: CameraSource? = null
    private var graphicOverlay: GraphicOverlay? = null

//    init {
//        surfaceView.holder.addCallback(SurfaceCallback())
//        addView(surfaceView)
//    }

    @RequiresPermission(Manifest.permission.CAMERA)
    fun start(cameraSource: CameraSource?) {
        if (cameraSource == null) stop()

        this.cameraSource = cameraSource

        if (this.cameraSource != null) {
            startRequested = true
            startIfReady()
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    fun start(cameraSource: CameraSource?, overlay: GraphicOverlay) {
        this.graphicOverlay = overlay
        start(cameraSource)
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    @Throws(IOException::class, SecurityException::class, RuntimeException::class)
    fun startIfReady() {
        if (startRequested) {
            cameraSource?.let { camera ->
                camera.start()
                graphicOverlay?.let {
                    val size = camera.previewSize
                    val min = min(size.width, size.height)
                    val max = max(size.width, size.height)
                    if (isPortraitMode()) {
                        it.setCameraInfo(min, max, camera.cameraFacing)
                    } else {
                        it.setCameraInfo(max, min, camera.cameraFacing)
                    }
                }
                graphicOverlay?.clear()
            }
            startRequested = false
        }
    }

//    @RequiresPermission(Manifest.permission.CAMERA)
//    @Throws(IOException::class, SecurityException::class, RuntimeException::class)
//    fun startIfReady() {
//        if (startRequested && surfaceAvailable) {
//            cameraSource?.let { cs ->
//                cs.start()
//                graphicOverlay?.let { go ->
//                    val size = cs.previewSize
//
//                    val min = Math.min(size.width, size.height)
//                    val max = Math.max(size.width, size.height)
//                    if (isPortraitMode()) {
//                        // Swap width and height sizes when in portrait, since it will be rotated by
//                        // 90 degrees
//                        graphicOverlay!!.setCameraInfo(min, max, cs.cameraFacing)
//                    } else {
//                        graphicOverlay!!.setCameraInfo(max, min, cs.cameraFacing)
//                    }
//                    overlay.clear()
//
//                }
//            }
//
//            startRequested = false
//        }
//    }

    fun stop() {
        cameraSource?.stop()
    }

    fun release() {
        cameraSource?.release()
        cameraSource = null
    }

    @SuppressLint("MissingPermission")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var previewWidth = 320
        var previewHeight = 240
        cameraSource?.let {
            val size = it.previewSize
            if (size != null) {
                previewWidth = size.width
                previewHeight = size.height
            }

        }

        if (isPortraitMode()) {
            val tmp = previewWidth
            previewWidth = previewHeight
            previewHeight = tmp
        }

        val viewWidth = right - left
        val viewHeight = bottom - top

        var childWidth = 0
        var childHeight = 0
        var childXOffset = 0
        var childYOffset = 0
        val widthRatio = viewWidth.toFloat() / previewWidth.toFloat()
        val heightRatio = viewHeight.toFloat() / previewHeight.toFloat()

        // To fill the view with the camera preview, while also preserving the correct aspect ratio,
        // it is usually necessary to slightly oversize the child and to crop off portions along one
        // of the dimensions.  We scale up based on the dimension requiring the most correction, and
        // compute a crop offset for the other dimension.

        if (widthRatio > heightRatio) {
            childWidth = viewWidth
            childHeight = (previewHeight * widthRatio).toInt()
            childYOffset = (childHeight - viewHeight) / 2
        } else {
            childWidth = (previewWidth * heightRatio).toInt()
            childHeight = viewHeight
            childXOffset = (childWidth - viewWidth) / 2
        }

        for (i in 0 until childCount) {
            // One dimension will be cropped.  We shift child over or up by this offset and adjust
            // the size to maintain the proper aspect ratio.
            getChildAt(i).layout(
                -1 * childXOffset, -1 * childYOffset,
                childWidth - childXOffset, childHeight - childYOffset
            )
        }

        try {
            startIfReady()
        } catch (e: Exception) {
            Log.e(TAG, "Could not start camera source.", e)
        }

    }

    private fun isPortraitMode(): Boolean {
        return when (context.resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> false
            Configuration.ORIENTATION_PORTRAIT -> true
            else -> {
                Log.d(TAG, "isPortraitMode returning false by default")
                false
            }
        }
    }

    inner class SurfaceCallback : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
            // leave empty as we don't need it
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            surfaceAvailable = false
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            surfaceAvailable = true

            try {
                startIfReady()
            } catch (se: SecurityException) {
                Log.e(TAG, "Do not have permission to start the camera", se)
            } catch (e: Exception) {
                Log.e(TAG, "Could not start camera source", e)
            }
        }
    }

    companion object {
        const val TAG = "CameraSourcePreview"
    }
}

