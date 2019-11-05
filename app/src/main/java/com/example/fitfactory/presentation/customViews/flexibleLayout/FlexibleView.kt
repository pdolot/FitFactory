package com.example.fitfactory.presentation.customViews.flexibleLayout

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.fitfactory.R
import kotlinx.android.synthetic.main.flexible_layout.view.*

class FlexibleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var oldScreenBrightness = 0
    private var minTranslation: Float = 0f
    private var thumbRect = Rect()
    var isViewEnable: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                visibility = if (value) View.VISIBLE else View.GONE
            }
        }

    init {
        View.inflate(context, R.layout.flexible_layout, this)
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                flexibleLayout_content.setPadding(
                    0,
                    context.resources.getDimensionPixelSize(R.dimen.topBarHeight),
                    0,
                    0
                )
                flexibleLayout_thumb.getLocalVisibleRect(thumbRect)
                minTranslation =
                    (-flexibleLayout_content.measuredHeight + context.resources.getDimensionPixelSize(
                        R.dimen.topBarHeight
                    )).toFloat()
                translationY = minTranslation
            }
        })
        setListeners()
        setQrCode()
    }

    private fun setQrCode() {
        Glide.with(context)
            .load("https://pl.qr-code-generator.com/wp-content/themes/qr/new_structure/markets/core_market/generator/dist/generator/assets/images/websiteQRCode_noFrame.png")
            .fitCenter()
            .into(flexibleLayout_qrCode)
    }

    private fun setListeners() {
        flexibleLayout_thumb.setOnTouchListener { v, event ->
            v as FlexibleButton
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (event.x >= (measuredWidth / 2) - (v.thumbSize / 2) && event.x <= (measuredWidth / 2) + (v.thumbSize / 2)) {
                        v.touchStart = true
                        true
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (v.touchStart && event.y >= thumbRect.top + v.thumbSize + v.barWidth && event.y <= thumbRect.bottom - v.barWidth) {
                        v.touchY = event.y
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (v.touchStart && event.y >= thumbRect.top + v.thumbSize) {
                        v.touchY = v.thumbSize + v.barWidth
                        v.touchStart = false
                        translateAnimation()
                    }
                }
            }
            true
        }

    }

    fun getThumbHeight(): Int {
        return flexibleLayout_thumb.height
    }

    fun forceClose() {
        translationY = minTranslation
        flexibleLayout_thumb.changeIcon(minTranslation)
    }

    private fun translateAnimation() {
        var translation = if (translationY == 0f) {
            minTranslation
        } else {
            0f
        }
        animate().apply {
            translationY(translation)
            duration = 1000
            interpolator = LinearInterpolator()
            withEndAction {
                flexibleLayout_thumb.changeIcon(translationY)
            }
            start()
        }
    }
}