package com.example.fitfactory.presentation.customViews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.fitfactory.R

class FrameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val linePaint = Paint().apply {
        //        color = ContextCompat.getColor(context, R.color.silverLight)
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private var strokeWidth = 4f
        set(value) {
            field = value
            linePaint.strokeWidth = field
        }
    private var color = 0
        set(value) {
            field = value
            linePaint.color = field
        }


    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.FrameView, defStyleAttr, 0)
        strokeWidth = a.getDimensionPixelSize(R.styleable.FrameView_strokeWidth, 4).toFloat()
        color = a.getColor(
            R.styleable.FrameView_color,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(getBound(), linePaint)
    }


    private fun getBound(): Path {
        val bound = Path()
        bound.reset()
        val dashWidth = (width * 2) / 7f

        bound.moveTo(dashWidth, strokeWidth / 2)
        bound.lineTo(strokeWidth / 2, strokeWidth / 2)
        bound.lineTo(strokeWidth / 2, dashWidth)

        bound.moveTo(strokeWidth / 2, height - dashWidth - strokeWidth / 2)
        bound.lineTo(strokeWidth / 2, height - strokeWidth / 2)
        bound.lineTo(dashWidth, height.toFloat() - strokeWidth / 2)

        bound.moveTo(width - dashWidth, height - strokeWidth / 2)
        bound.lineTo(width - strokeWidth / 2, height - strokeWidth / 2)
        bound.lineTo(width - strokeWidth / 2, height - dashWidth)

        bound.moveTo(width - strokeWidth / 2, dashWidth)
        bound.lineTo(width - strokeWidth / 2, strokeWidth / 2)
        bound.lineTo(width - dashWidth, strokeWidth / 2)

        return bound
    }
}