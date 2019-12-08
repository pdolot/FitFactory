package com.example.fitfactory.presentation.customViews.singleChoiceDialog

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitfactory.R
import kotlinx.android.synthetic.main.view_single_choice_dialog.view.*

class SingleChoiceDialog @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var items: List<Pair<Long, String>>? = null

    init {
        View.inflate(context, R.layout.view_single_choice_dialog, this)
    }

    fun setData(items : List<Pair<Long, String>>?){
        this.items = items
        setRadioGroup()
    }

    private fun setRadioGroup() {
        items?.forEach {
            val item = RadioButton(context)
            item.id = it.first.toInt()
            item.text = it.second
            radioGroup.addView(item)
        }
    }
}