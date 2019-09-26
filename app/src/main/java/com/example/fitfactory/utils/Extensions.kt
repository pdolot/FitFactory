package com.example.fitfactory.utils

fun Float.scaleValue(a: Float, b: Float, c: Float, d: Float): Float {
    return (this - a) * (d - c) / (b - a) + c
}

fun Int.scaleValue(a: Float, b: Float, c: Float, d: Float): Float {
    return (this - a) * (d - c) / (b - a) + c
}