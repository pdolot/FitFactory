package com.example.fitfactory.presentation.customViews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.example.fitfactory.R

class MeshView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val linePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private var rows = 10
    private var cols = 20

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
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MeshView, defStyleAttr, 0)
        strokeWidth = a.getDimensionPixelSize(R.styleable.MeshView_strokeWidth, 4).toFloat()
        rows = a.getInteger(R.styleable.MeshView_rows, 1)
        cols = a.getInteger(R.styleable.MeshView_cols, 1)
        color = a.getColor(
            R.styleable.MeshView_color,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        a.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        getBound(canvas)
//        canvas.drawPath(getBound(canvas), linePaint)
    }


    private fun getBound(canvas: Canvas): Path {
        val horizontalLine = Path()
        val verticalLine = Path()


        var jumpY = (measuredHeight - strokeWidth) / (rows - 1)
        var jumpX = (measuredWidth - strokeWidth) / (cols - 1)

        for (i in 0 until rows) {
            horizontalLine.reset()
            val y = strokeWidth / 2 + (jumpY * i)
            horizontalLine.moveTo(0f, y)
            horizontalLine.lineTo(measuredWidth.toFloat(), y)
            val lineColor =
                ColorUtils.setAlphaComponent(this@MeshView.color, 255 - (255 * i / rows))

            for (j in 0 until cols) {
                val x = strokeWidth / 2 + (jumpX * j)
                verticalLine.reset()
                verticalLine.moveTo(x, y + (strokeWidth / 2))
                verticalLine.lineTo(x, y + jumpY - (strokeWidth / 2))
                canvas.drawPath(verticalLine, linePaint.apply {
                    color = lineColor
                })
            }

            canvas.drawPath(horizontalLine, linePaint)
        }

        return horizontalLine
    }
}