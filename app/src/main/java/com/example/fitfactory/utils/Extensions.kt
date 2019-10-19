package com.example.fitfactory.utils

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import java.sql.Time
import kotlin.math.floor

fun Float.scaleValue(a: Float, b: Float, c: Float, d: Float): Float {
    return (this - a) * (d - c) / (b - a) + c
}

fun Float.rest(): Float {
    return this - floor(this)
}

fun Int.scaleValue(a: Float, b: Float, c: Float, d: Float): Float {
    return (this - a) * (d - c) / (b - a) + c
}

fun Time.isBetween(start: String, end: String): Boolean {
    return this in Time.valueOf(start)..Time.valueOf(end)
}

fun TextInputEditText.addMaskAndTextWatcher(mask: String) {
    this.addTextChangedListener(object : TextWatcher {
        private var isRunning = false
        private var isDeleting = false

        override fun afterTextChanged(s: Editable) {
            if (isDeleting) {
                if (mask[s.length] != '#') {
                    s.delete(s.length - 1, s.length)
                }
            }
            if (isRunning || isDeleting) return
            isRunning = true

            if (s.length < mask.length) {
                if (mask[s.length] != '#') {
                    s.append(mask[s.length])
                } else if (mask[s.length - 1] != '#') {
                    s.insert(s.length - 1, mask, s.length, s.length - 1)
                }
            }

            isRunning = false
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            isDeleting = count > after
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}
