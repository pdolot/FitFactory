package com.example.fitfactory.presentation.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.drawerlayout.widget.DrawerLayout

class CustomDrawerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : DrawerLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(
            MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY
        )

        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(
            MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}