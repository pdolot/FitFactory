package com.example.fitfactory.presentation.components

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

class FlexibleBottomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var minTranslation: Float = 0f
    private var maxTranslation: Float = 0f
    private var topBarId: Int = 0
    private var topBarSize: Float = 0f
    private var contentRect = Rect()
    private var thumbRect = Rect()

    init {
        init(context)
        init(context, attrs)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.flexible_layout, this)
        setQrCode()
    }

    private fun setQrCode() {
        Glide.with(context)
            .load("https://pl.qr-code-generator.com/wp-content/themes/qr/new_structure/markets/core_market/generator/dist/generator/assets/images/websiteQRCode_noFrame.png")
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
                        v.updateView()
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (v.touchStart && event.y >= thumbRect.top + v.thumbSize) {
                        v.touchY = v.thumbSize + v.barWidth
                        v.touchStart = false
                        v.updateView()
                        translateAnimation(translationY)
                    }
                }
            }
            true
        }

    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.style
        )
        val count = typedArray.indexCount

        try {
            for (i in 0 until count) {
                val attr = typedArray.getIndex(i)
                when (attr) {
                    R.styleable.style_topBarId -> {
                        topBarId = typedArray.getResourceId(attr, 0)
                    }
                }
            }
        } finally {
            typedArray.recycle()
        }

        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                var view = rootView.findViewById<View>(topBarId)
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                topBarSize = view.height.toFloat()
                flexibleLayout_content.setPadding(0, topBarSize.toInt(), 0, 0)

                flexibleLayout_content.getLocalVisibleRect(contentRect)
                flexibleLayout_thumb.getLocalVisibleRect(thumbRect)
                minTranslation = (-contentRect.bottom + topBarSize)
                translationY = minTranslation
            }

        })
        setListeners()
    }


    private fun translateAnimation(translationY: Float) {

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
                flexibleLayout_thumb.changeIcon(translation)
            }
            start()
        }
    }

}