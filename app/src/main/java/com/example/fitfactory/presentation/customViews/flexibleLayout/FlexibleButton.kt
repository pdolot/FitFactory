package com.example.fitfactory.presentation.customViews.flexibleLayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.example.fitfactory.R


class FlexibleButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var touchStart: Boolean = false
    var thumbSize: Float = 0f
    private var iconDown: Drawable? = null
    private var iconUp: Drawable? = null
    private var icon: Drawable? = null
    private var iconColor: Int = 0
    private var viewColor: Int = 0
    private var viewPaint: Paint = Paint()
    private var path: Path = Path()
    var barWidth: Float = 10f

    var touchY: Float = 0f
    private lateinit var firstPoint: Point
    private lateinit var firstPointControl: Point
    private lateinit var endPoint: Point
    private lateinit var endPointControl: Point
    private lateinit var centerPoint: Point
    private lateinit var centerPointControlLeft: Point
    private lateinit var centerPointControlRight: Point
    private lateinit var iconBound: Bound

    init {
        init(context, attrs)
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
                    R.styleable.style_thumbSize -> {
                        thumbSize = typedArray.getFloat(attr, 100f)
                        touchY = thumbSize
                    }
                    R.styleable.style_iconDown -> {
                        iconDown = typedArray.getDrawable(attr)
                        icon = iconDown
                    }
                    R.styleable.style_iconUp -> {
                        iconUp = typedArray.getDrawable(attr)
                    }
                    R.styleable.style_iconColor -> {
                        iconColor = typedArray.getColor(attr, resources.getColor(R.color.primaryDark))
                    }
                    R.styleable.style_color -> {
                        viewColor = typedArray.getColor(attr, resources.getColor(R.color.primaryMedium))
                    }
                }
            }
        } finally {
            typedArray.recycle()
        }

        viewPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
            color = viewColor
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            measureDimension(suggestedMinimumWidth, widthMeasureSpec),
            measureDimension(touchY.toInt() * 2, heightMeasureSpec)
        )
    }

    private fun measurePoints() {
        firstPoint = Point((measuredWidth.toFloat() / 2) - (thumbSize * 2), barWidth)
        firstPointControl = Point((measuredWidth.toFloat() / 2) - thumbSize, barWidth)
        centerPointControlLeft = Point((measuredWidth.toFloat() / 2) - thumbSize, touchY + barWidth)
        centerPoint = Point((measuredWidth.toFloat() / 2), touchY + barWidth)
        centerPointControlRight = Point((measuredWidth.toFloat() / 2) + thumbSize, touchY + barWidth)
        endPoint = Point((measuredWidth.toFloat() / 2) + (thumbSize * 2), barWidth)
        endPointControl = Point((measuredWidth.toFloat() / 2) + thumbSize, barWidth)

        iconBound = Bound(
            (measuredWidth / 2) - (thumbSize / 2 + barWidth).toInt(),
            (touchY - thumbSize + barWidth).toInt(),
            (measuredWidth / 2) + (thumbSize / 2 + barWidth).toInt(),
            (touchY + barWidth).toInt()
        )
    }

    override fun onDraw(canvas: Canvas) {
        measurePoints()
        path.reset()

        path.moveTo(0f, 0f)
        path.lineTo(measuredWidth.toFloat(), 0f)
        path.lineTo(measuredWidth.toFloat(), barWidth)
        path.lineTo(firstPoint.x, firstPoint.y)
        path.cubicTo(
            firstPointControl.x,
            firstPointControl.y,
            centerPointControlLeft.x,
            centerPointControlLeft.y,
            centerPoint.x,
            centerPoint.y
        )
        path.cubicTo(
            centerPointControlRight.x,
            centerPointControlRight.y,
            endPointControl.x,
            endPointControl.y,
            endPoint.x,
            endPoint.y
        )
        path.lineTo(0f, barWidth)
        path.close()
        canvas.drawPath(path, viewPaint)

        icon?.setTint(iconColor)
        icon?.setBounds(iconBound.left, iconBound.top, iconBound.right, iconBound.bottom)
        icon?.draw(canvas)
        super.onDraw(canvas)
    }

    fun updateView() {
        invalidate()
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
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    fun changeIcon(translation: Float) {
        icon = if (translation == 0f) {
            iconUp
        } else {
            iconDown
        }
        invalidate()
    }

    inner class Point(var x: Float, var y: Float)
    inner class Bound(var left: Int, var top: Int, var right: Int, var bottom: Int)
}


