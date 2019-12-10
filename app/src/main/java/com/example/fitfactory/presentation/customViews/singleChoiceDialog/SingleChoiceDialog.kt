package com.example.fitfactory.presentation.customViews.singleChoiceDialog

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.fitfactory.R
import kotlinx.android.synthetic.main.view_single_choice_dialog.view.*

class SingleChoiceDialog @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var items: List<Pair<Long, String>>? = null
    private val textColor = ContextCompat.getColor(context, R.color.silverLight)
    private val fontFamily = ResourcesCompat.getFont(context, R.font.fira_sans_condensed_medium)
    private val paddingTopBottom =  resources.getDimensionPixelSize(R.dimen.mini_margin)
    private val paddingLeftRight =  resources.getDimensionPixelSize(R.dimen.small_margin)

    init {
        View.inflate(context, R.layout.view_single_choice_dialog, this)
    }

    fun setData(items : List<Pair<Long, String>>?){
        this.items = items
        setRadioGroup()
    }

    private fun setRadioGroup() {
        val item = RadioButton(context)
        item.setTextColor(textColor)
        item.typeface = fontFamily
        item.buttonTintList = ColorStateList.valueOf(textColor)
        item.setPadding(paddingLeftRight, paddingTopBottom,paddingLeftRight, paddingTopBottom)
        item.id = 0
        item.text = "Pokaż wszystkie"
        radioGroup.addView(item)

        items?.forEach {
            val item = RadioButton(context)
            item.setTextColor(textColor)
            item.typeface = fontFamily
            item.buttonTintList = ColorStateList.valueOf(textColor)
            item.setPadding(paddingLeftRight, paddingTopBottom,paddingLeftRight, paddingTopBottom)
            item.id = it.first.toInt()
            item.text = it.second
            radioGroup.addView(item)
        }
    }

    fun getSelectedItemId() = radioGroup.checkedRadioButtonId
}