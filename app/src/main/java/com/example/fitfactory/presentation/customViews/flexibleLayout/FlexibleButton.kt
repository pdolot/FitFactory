package com.example.fitfactory.presentation.customViews.flexibleLayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.example.fitfactory.R
import kotlin.math.min


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
        set(value) {
            field = value
            measurePoints()
            invalidate()
        }
    private lateinit var center: Point
    private lateinit var sp: Point // start point
    private lateinit var lp: Point  // left point control
    private lateinit var ep: Point // end point
    private lateinit var rp: Point // right point control
    private lateinit var cp: Point // center point
    private lateinit var clp: Point // center left point control
    private lateinit var crp: Point // center right point control
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

                when (val attr = typedArray.getIndex(i)) {
                    R.styleable.style_thumbSize -> {
                        thumbSize = typedArray.getDimension(attr, 10f)
                    }
                    R.styleable.style_barWidth -> {
                        barWidth = typedArray.getDimension(attr, 10f)
                    }
                    R.styleable.style_iconDown -> {
                        iconDown = typedArray.getDrawable(attr)
                        icon = iconDown
                    }
                    R.styleable.style_iconUp -> {
                        iconUp = typedArray.getDrawable(attr)
                    }
                    R.styleable.style_iconColor -> {
                        iconColor =
                            typedArray.getColor(attr, resources.getColor(R.color.primaryDark))
                    }
                    R.styleable.style_color -> {
                        viewColor =
                            typedArray.getColor(attr, resources.getColor(R.color.primaryMedium))
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
            measureDimension(thumbSize.toInt() * 2 + barWidth.toInt(), heightMeasureSpec)
        )
        touchY = thumbSize + barWidth
    }

    private fun measurePoints() {
        center = Point(measuredWidth / 2f, touchY - (thumbSize / 2))
        sp = Point(center.x - (thumbSize * 2), barWidth)
        lp = Point(center.x - thumbSize, barWidth)
        clp = Point(center.x - thumbSize, touchY)
        cp = Point(center.x, touchY)
        crp = Point(center.x + thumbSize, touchY)
        rp = Point(center.x + thumbSize, barWidth)
        ep = Point(center.x + (thumbSize * 2), barWidth)

        iconBound = Bound(
            (center.x - thumbSize / 2).toInt(),
            (center.y - thumbSize / 2).toInt(),
            (center.x + thumbSize / 2).toInt(),
            (center.y + thumbSize / 2).toInt()
        )
    }

    override fun onDraw(canvas: Canvas) {
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(measuredWidth.toFloat(), 0f)
        path.lineTo(measuredWidth.toFloat(), barWidth)
        path.lineTo(sp.x, sp.y)
        path.cubicTo(
            lp.x, lp.y,
            clp.x, clp.y,
            cp.x, cp.y
        )
        path.cubicTo(
            crp.x, crp.y,
            rp.x, rp.y,
            ep.x, ep.y
        )
        path.lineTo(0f, barWidth)
        path.close()
        canvas.drawPath(path, viewPaint)

        icon?.setTint(iconColor)
        icon?.setBounds(iconBound.left, iconBound.top, iconBound.right, iconBound.bottom)
        icon?.draw(canvas)
        super.onDraw(canvas)
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


