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
package com.example.fitfactory.presentation.pages.exercises.barcodeScanner

import android.graphics.Bitmap
import com.google.firebase.ml.common.FirebaseMLException
import java.nio.ByteBuffer

/** An inferface to process the images with different ML Kit detectors and custom image models. */
interface VisionImageProcessor {

    /** Processes the images with the underlying machine learning models. */
    @Throws(FirebaseMLException::class)
    fun process(data: ByteBuffer, frameMetadata: FrameMetadata, graphicOverlay: GraphicOverlay)

    /** Processes the bitmap images. */
    fun process(bitmap: Bitmap, graphicOverlay: GraphicOverlay)

    /** Stops the underlying machine learning model and release resources. */
    fun stop()
}