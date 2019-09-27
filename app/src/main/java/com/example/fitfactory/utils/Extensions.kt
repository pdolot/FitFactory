package com.example.fitfactory.utils

import kotlin.math.floor

fun Float.scaleValue(a: Float, b: Float, c: Float, d: Float): Float {
    return (this - a) * (d - c) / (b - a) + c
}

fun Float.rest(): Float{
    return this - floor(this)
}

fun Int.scaleValue(a: Float, b: Float, c: Float, d: Float): Float {
    return (this - a) * (d - c) / (b - a) + c
}
