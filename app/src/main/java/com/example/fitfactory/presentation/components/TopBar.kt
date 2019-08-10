package com.example.fitfactory.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitfactory.R
import kotlinx.android.synthetic.main.top_bar.view.*

class TopBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        init(context)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.top_bar, this)
    }

    fun setTitle(title: String) {
        topBar_title.text = title
    }
}