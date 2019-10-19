package com.example.fitfactory.presentation.customViews.buttonLayout

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.*

class OverlappingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var buttonRadius: Float = 100f
    private var spaceWidth: Float = 10f
    private var roundRadius: Float = 30f
    private lateinit var centerPoint: Point
    private lateinit var firstIntersectionPoint: Point
    private lateinit var secondIntersectionPoint: Point
    private var roundAngle = 0f
    private lateinit var firstArcAngle: Arc
    private lateinit var secondArcAngle: Arc
    private lateinit var centerArcAngle: Arc
    private lateinit var leftRoundBound: RectF
    private lateinit var rightRoundBound: RectF
    private lateinit var outlineCircleBound: RectF
    private lateinit var thumbBound: RectF
    var icon: Drawable? = null
        set(value) {
            field = value
            invalidate()
        }
    var iconTint: Int = 0
    var disabledIconTint: Int = 0

    var clickListener: (Boolean) -> Unit = {}

    var clickEnable = true
        set(value) {
            field = value
            thumbPaint.color = if (field) thumbColor else disableColor
            invalidate()
        }

    var bgColor = 0
        set(value) {
            field = value
            backgroundPaint.color = field
            invalidate()
        }

    private var thumbColor = 0
    private var disableColor = 0

    private var thumbPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private var backgroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    init {
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (event.x in thumbBound.left..thumbBound.right && event.y in thumbBound.top..thumbBound.bottom) {
                        clickListener(clickEnable)
                        true
                    }
                }
            }
            false
        }
    }

    fun setValues(
        buttonRadius: Float,
        spaceWidth: Float,
        roundRadius: Float,
        bgColor: Int,
        thumbColor: Int,
        disableColor: Int
    ) {
        this.buttonRadius = buttonRadius
        this.spaceWidth = spaceWidth
        this.roundRadius = roundRadius
        this.bgColor = bgColor
        this.thumbColor = thumbColor
        this.disableColor = disableColor
        measureIntersectionPoints()
        measureBound()
        invalidate()
    }

    fun getCenterY() = centerPoint.y

    private fun measureBound() {
        leftRoundBound = RectF(
            firstIntersectionPoint.x - roundRadius,
            firstIntersectionPoint.y - roundRadius,
            firstIntersectionPoint.x + roundRadius,
            firstIntersectionPoint.y + roundRadius
        )

        rightRoundBound = RectF(
            secondIntersectionPoint.x - roundRadius,
            secondIntersectionPoint.y - roundRadius,
            secondIntersectionPoint.x + roundRadius,
            secondIntersectionPoint.y + roundRadius
        )

        outlineCircleBound = RectF(
            centerPoint.x - (buttonRadius + spaceWidth),
            centerPoint.y - (buttonRadius + spaceWidth),
            centerPoint.x + (buttonRadius + spaceWidth),
            centerPoint.y + (buttonRadius + spaceWidth)
        )

        thumbBound = RectF(
            centerPoint.x - buttonRadius,
            centerPoint.y - buttonRadius,
            centerPoint.x + buttonRadius,
            centerPoint.y + buttonRadius
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            measureDimension(
                (buttonRadius + spaceWidth + roundRadius).toInt() * 2,
                widthMeasureSpec
            ),
            measureDimension((buttonRadius * 2 + spaceWidth).toInt(), heightMeasureSpec)
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

    private fun measureIntersectionPoints() {
        centerPoint = Point(
            (buttonRadius + spaceWidth + roundRadius).toInt(),
            -(spaceWidth + buttonRadius).toInt()
        )
        val y = centerPoint.y + roundRadius
        val outlineCircleRadius = buttonRadius + spaceWidth + roundRadius
        val b = -2f * centerPoint.x
        val c =
            centerPoint.x.toFloat().pow(2f) + (y - centerPoint.y).pow(2f) - outlineCircleRadius.pow(
                2f
            )
        val delta = b.pow(2f) - (4 * c)

        var x1 = (-b - sqrt(delta)) / 2
        var x2 = (-b + sqrt(delta)) / 2

        firstIntersectionPoint = Point(x1.roundToInt(), -y.toInt())
        secondIntersectionPoint = Point(x2.roundToInt(), -y.toInt())
        val d = sqrt((centerPoint.x - x1).pow(2f) + (centerPoint.y - y).pow(2f))
        roundAngle = sin(roundRadius / d) * 57.29577951308f

        firstArcAngle = Arc(90f, -(90f - roundAngle))
        centerArcAngle = Arc(180f + roundAngle, 180f - (roundAngle * 2))
        secondArcAngle = Arc(180f - roundAngle, -(90f - roundAngle))

        centerPoint.y *= -1
    }

    inner class Arc(val startAngle: Float, val sweepAngle: Float)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val path = Path()
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(0f, centerPoint.y.toFloat())
        path.lineTo(firstIntersectionPoint.x.toFloat(), centerPoint.y.toFloat())
        path.arcTo(leftRoundBound, firstArcAngle.startAngle, firstArcAngle.sweepAngle, false)
        path.arcTo(outlineCircleBound, centerArcAngle.startAngle, centerArcAngle.sweepAngle, false)
        path.arcTo(rightRoundBound, secondArcAngle.startAngle, secondArcAngle.sweepAngle, false)
        path.lineTo(measuredWidth.toFloat(), centerPoint.y.toFloat())
        path.lineTo(measuredWidth.toFloat(), 0f)
        path.lineTo(0f, 0f)
        path.close()

        canvas.drawPath(path, backgroundPaint)
        canvas.drawOval(thumbBound, thumbPaint)

        icon?.apply {
            setBounds(
                (thumbBound.left + buttonRadius * 0.4).toInt(),
                (thumbBound.top + buttonRadius * 0.4).toInt(),
                (thumbBound.right - buttonRadius * 0.4).toInt(),
                (thumbBound.bottom - buttonRadius * 0.4).toInt()
            )
            setTint(if (clickEnable) iconTint else disabledIconTint)
            draw(canvas)
        }
    }
}