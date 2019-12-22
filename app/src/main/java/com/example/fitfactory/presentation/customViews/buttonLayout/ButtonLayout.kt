package com.example.fitfactory.presentation.customViews.buttonLayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.fitfactory.R
import kotlinx.android.synthetic.main.view_button_layout.view.*

class ButtonLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var bias = 0.5f
    private var bgColor = 0

    var isEnable: Boolean? = null
        set(value) {
            field = value
            field?.let { overlappingButton.clickEnable = it }

        }

    init {
        View.inflate(context, R.layout.view_button_layout, this)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.style, defStyleAttr, 0)
        overlappingButton.setValues(
            a.getDimension(R.styleable.style_buttonRadius, 100f),
            a.getDimension(R.styleable.style_spaceWidth, 100f),
            a.getDimension(R.styleable.style_roundRadius, 100f),
            a.getColor(
                R.styleable.style_backgroundColor,
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            ),
            a.getColor(
                R.styleable.style_thumbColor,
                ContextCompat.getColor(context, R.color.colorAccent)
            ),
            a.getColor(
                R.styleable.style_disableColor,
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
        )

        bgColor = a.getColor(R.styleable.style_backgroundColor, ContextCompat.getColor(context, R.color.colorPrimaryDark))

        val params = paddingTopView.layoutParams
        params.height = a.getDimensionPixelSize(R.styleable.style_paddingTop, 0)
        paddingTopView.layoutParams = params


        overlappingButton.clickEnable = a.getBoolean(R.styleable.style_isEnable, true)
        overlappingButton.icon = a.getDrawable(R.styleable.style_icon)
        overlappingButton.iconTint = a.getColor(
            R.styleable.style_iconColor,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        overlappingButton.disabledIconTint = a.getColor(
            R.styleable.style_disabledIconColor,
            ContextCompat.getColor(context, R.color.colorPrimaryDark)
        )

        bias = a.getFloat(R.styleable.style_bias, 0.5f)

        listOf<View>(leftView, rightView).forEach {
            it.layoutParams.apply {
                height = overlappingButton.getCenterY()
            }
            it.background.setTint(bgColor)
        }
        paddingTopView.background.setTint(bgColor)
        a.recycle()

        (overlappingButton.layoutParams as LayoutParams).apply {
            this.horizontalBias = bias
        }
    }

    fun setButtonClickListener(function : (Boolean) -> Unit){
        overlappingButton.clickListener = function
    }
}