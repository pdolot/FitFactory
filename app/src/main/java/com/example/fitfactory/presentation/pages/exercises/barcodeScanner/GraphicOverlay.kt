package com.example.fitfactory.presentation.pages.exercises.barcodeScanner

// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.google.android.gms.vision.CameraSource
import java.util.*

/**
 * A view which renders a series of custom graphics to be overlayed on top of an associated preview
 * (i.e., the camera preview). The creator can add graphics objects, update the objects, and remove
 * them, triggering the appropriate drawing and invalidation within the view.
 *
 * <p>Supports scaling and mirroring of the graphics relative the camera's preview properties. The
 * idea is that detection items are expressed in terms of a preview size, but need to be scaled up
 * to the full view size, and also mirrored in the case of the front-facing camera.
 *
 * <p>Associated {@link Graphic} items should use the following methods to convert to view
 * coordinates for the graphics that are drawn:
 *
 * <ol>
 *   <li>{@link Graphic#scaleX(float)} and {@link Graphic#scaleY(float)} adjust the size of the
 *       supplied value from the preview scale to the view scale.
 *   <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)} adjust the
 *       coordinate from the preview's coordinate system to the view coordinate system.
 * </ol>
 */
class GraphicOverlay @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val lock: Object = Object()
    private var previewWidth: Int = 0
    private var widthScaleFactor: Float = 1.0f
    private var previewHeight: Int = 0
    private var heightScaleFactor: Float = 1.0f
    private var facing = CameraSource.CAMERA_FACING_BACK
    private val graphics = ArrayList<Graphic>()

    abstract class Graphic(private var overlay: GraphicOverlay) {
        /**
         * Draw the graphic on the supplied canvas. Drawing should use the following methods to convert
         * to view coordinates for the graphics that are drawn:
         *
         * <ol>
         *   <li>{@link Graphic#scaleX(float)} and {@link Graphic#scaleY(float)} adjust the size of the
         *       supplied value from the preview scale to the view scale.
         *   <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)} adjust the
         *       coordinate from the preview's coordinate system to the view coordinate system.
         * </ol>
         *
         * @param canvas drawing canvas
         */
        abstract fun draw(canvas: Canvas)

        /**
         * Adjusts a horizontal value of the supplied value from the preview scale to the view scale.
         */
        fun scaleX(horizontal: Float): Float {
            return horizontal * overlay.widthScaleFactor
        }

        /** Adjusts a vertical value of the supplied value from the preview scale to the view scale. */
        fun scaleY(vertical: Float): Float{
            return vertical * overlay.heightScaleFactor
        }


        /** Returns the application context of the app. */
        fun getApplicationContext() = overlay.context.applicationContext

        /**
         * Adjusts the x coordinate from the preview's coordinate system to the view coordinate system.
         */
        fun translateX(x: Float): Float =
            if (overlay.facing == CameraSource.CAMERA_FACING_FRONT) {
                overlay.width - scaleX(x)
            } else {
                scaleX(x)
            }

        /**
         * Adjusts the y coordinate from the preview's coordinate system to the view coordinate system.
         */

        fun translateY(y: Float): Float = scaleY(y)

        fun postInvalidate() {
            overlay.postInvalidate()
        }
    }

    /** Removes all graphics from the overlay. */
    fun clear() {
        synchronized(lock) {
            graphics.clear()
        }
        postInvalidate()
    }

    /** Adds a graphic to the overlay. */
    fun add(graphic: Graphic) {
        synchronized(lock) {
            graphics.add(graphic)
        }
    }

    /** Removes a graphic from the overlay. */
    fun remove(graphic: Graphic) {
        synchronized(lock) {
            graphics.remove(graphic)
        }
        postInvalidate()
    }

    /**
     * Sets the camera attributes for size and facing direction, which informs how to transform image
     * coordinates later.
     */
    fun setCameraInfo(previewWidth: Int, previewHeight: Int, facing: Int) {
        synchronized(lock) {
            this.previewWidth = previewWidth
            this.previewHeight = previewHeight
            this.facing = facing
        }
        postInvalidate()
    }

    /** Draws the overlay with its associated graphic objects. */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        synchronized(lock) {
            if (previewWidth != 0 && previewHeight != 0) {
                widthScaleFactor = width / previewWidth.toFloat()
                heightScaleFactor = height / previewHeight.toFloat()
            }

            for (graphic in graphics) {
                graphic.draw(canvas)
            }

        }
    }


    /**
     * Base class for a custom graphics object to be rendered within the graphic overlay. Subclass
     * this and implement the {@link Graphic#draw(Canvas)} method to define the graphics element. Add
     * instances to the overlay using {@link GraphicOverlay#add(Graphic)}.
     */


}