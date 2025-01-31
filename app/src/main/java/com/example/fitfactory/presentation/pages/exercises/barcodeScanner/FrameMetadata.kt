package com.example.fitfactory.presentation.pages.exercises.barcodeScanner

class FrameMetadata(
    val width: Int, val height: Int,
    val rotation: Int, val cameraFacing: Int
) {

    companion object{
        class Builder {
            private var width: Int = 0
            private var height: Int = 0
            private var rotation: Int = 0
            private var cameraFacing: Int = 0

            fun setWidth(width: Int): Builder {
                this.width = width
                return this
            }

            fun setHeight(height: Int): Builder {
                this.height = height
                return this
            }

            fun setRotation(rotation: Int): Builder {
                this.rotation = rotation
                return this
            }

            fun setCameraFacing(cameraFacing: Int): Builder {
                this.cameraFacing = cameraFacing
                return this
            }

            fun build(): FrameMetadata = FrameMetadata(width, height, rotation, cameraFacing)
        }
    }

}