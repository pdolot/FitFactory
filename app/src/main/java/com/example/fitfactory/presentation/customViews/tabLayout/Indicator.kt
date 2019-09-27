package com.example.fitfactory.presentation.customViews.tabLayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.fitfactory.R
import kotlin.math.min

class Indicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var indicatorRadius: Int = 18
    private var top: Float = 0f
    private var left: Float = 0f
    private var bottom: Float = 0f
    private var right: Float = 0f
    var indicatorColor: Int? = null
        set(value) {
            field = value
            indicatorPaint.color = value ?: ContextCompat.getColor(context, R.color.colorPrimary)
            invalidate()
        }

    private var indicatorPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = indicatorColor ?: ContextCompat.getColor(context, R.color.colorAccent)
        style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            measureDimension(measuredWidth, widthMeasureSpec),
            measureDimension(indicatorRadius * 5, heightMeasureSpec)
        )
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }

    fun setBounds(left: Float, top: Float, right: Float, bottom: Float) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRoundRect(
            left,
            top,
            right,
            bottom,
            indicatorRadius.toFloat(),
            indicatorRadius.toFloat(),
            indicatorPaint
        )
    }

}