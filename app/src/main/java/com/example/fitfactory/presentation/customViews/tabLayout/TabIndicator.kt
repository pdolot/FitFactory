package com.example.fitfactory.presentation.customViews.tabLayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.fitfactory.R
import com.example.fitfactory.utils.Bound
import kotlin.math.floor
import kotlin.math.min

class TabIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var indicatorRadius: Int = 18
    var itemCount = 1
        set(value) {
            field = value
            measureItemInRow()
        }
    private var spaceWidth = 0
    private var indicatorsPositions: ArrayList<Bound> = ArrayList()
    private var indicatorsBgPositions: ArrayList<Bound> = ArrayList()
    private val indicatorBackground = ContextCompat.getDrawable(context, R.drawable.indicator_bg)
    private lateinit var tabIndicatorListener: TabIndicatorListener

    var maxItemCountInRow = 1
        set(value) {
            field = value
            measureItemInRow()
        }


    private fun measureItemInRow(){
        itemInRow = if (itemCount <= maxItemCountInRow) itemCount else maxItemCountInRow
        invalidate()
    }

    var itemInRow: Int? = null
        set(value) {
            field = value
            measurePoints()
            invalidate()
        }

    var indicatorColor: Int? = null
        set(value) {
            field = value
            indicatorPaint.color = value ?: ContextCompat.getColor(context, R.color.colorPrimary)
            invalidate()
        }
    var bgColor: Int? = null
        set(value) {
            field = value
            backgroundPaint.color =
                value ?: ContextCompat.getColor(context, R.color.colorPrimaryDark)
            invalidate()
        }

    private var indicatorPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private var backgroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    init {
        setListener()
    }

    private fun setListener() {
        setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    measureCurrentItem(motionEvent.x)?.let {
                        tabIndicatorListener.onItemSelected(it)
                        true
                    }
                }
            }
            false
        }
    }

    private fun measureCurrentItem(x: Float): Int? {
        for (i in indicatorsBgPositions.indices) {
            if (x >= indicatorsBgPositions[i].left && x <= indicatorsBgPositions[i].right) {
                return i
            }
        }
        return null
    }

    fun setTabIndicatorListener(tabIndicatorListener: TabIndicatorListener) {
        this.tabIndicatorListener = tabIndicatorListener
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            measureDimension(measuredWidth, widthMeasureSpec),
            measureDimension(indicatorRadius * 5, heightMeasureSpec)
        )

        maxItemCountInRow = floor(measuredWidth / (indicatorRadius * 5.0)).toInt()
    }

    private fun measurePoints(){
        itemInRow?.let {
            if (it != 0){
                val spaceSize = measuredWidth / it
                val startPosition = spaceSize / 2
                indicatorsPositions.clear()
                indicatorsBgPositions.clear()
                for (i in 0 until it) {
                    val centerPosition = startPosition + (spaceSize * i)
                    indicatorsPositions.add(
                        Bound(
                            centerPosition - indicatorRadius,
                            (measuredHeight / 2) - indicatorRadius,
                            centerPosition + indicatorRadius,
                            (measuredHeight / 2) + indicatorRadius
                        )
                    )
                    indicatorsBgPositions.add(
                        Bound(
                            centerPosition - (measuredHeight / 2),
                            0,
                            centerPosition + (measuredHeight / 2),
                            measuredHeight
                        )
                    )
                }

                spaceWidth = indicatorsBgPositions[0].left
                if (spaceWidth % 2 != 0) {
                    spaceWidth +=  1
                }
            }
        }

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        itemInRow?.let {
            for (i in 0 until it) {
                val indicatorPosition = indicatorsPositions[i]
                val indicatorBgPosition = indicatorsBgPositions[i]
                canvas.drawOval(
                    indicatorPosition.left.toFloat(),
                    indicatorPosition.top.toFloat(),
                    indicatorPosition.right.toFloat(),
                    indicatorPosition.bottom.toFloat(),
                    indicatorPaint
                )

                val d = indicatorBackground
                d?.setTint(backgroundPaint.color)
                d?.setBounds(
                    indicatorBgPosition.left,
                    indicatorBgPosition.top,
                    indicatorBgPosition.right,
                    indicatorBgPosition.bottom
                )
                d?.draw(canvas)
                canvas.drawRect(
                    indicatorBgPosition.left.toFloat() - spaceWidth,
                    0f,
                    indicatorBgPosition.left.toFloat(),
                    ((measuredHeight / 2) - (indicatorRadius / 2)).toFloat(),
                    backgroundPaint
                )
                canvas.drawRect(
                    indicatorBgPosition.right.toFloat(),
                    0f,
                    indicatorBgPosition.right.toFloat() + spaceWidth,
                    ((measuredHeight / 2) - (indicatorRadius / 2)).toFloat(),
                    backgroundPaint
                )
            }
        }

    }

    fun getPositionAt(index: Int): Bound {
        return indicatorsPositions[index]
    }

    interface TabIndicatorListener {
        fun onItemSelected(position: Int)
    }

}