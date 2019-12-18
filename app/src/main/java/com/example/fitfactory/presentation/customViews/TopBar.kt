package com.example.fitfactory.presentation.customViews

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.fitfactory.R
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import kotlinx.android.synthetic.main.top_bar.view.*
import javax.inject.Inject

class TopBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var topBarListener: TopBarListener
    var topBarTitleChangeListener: (String) -> Unit = {}

    init {
        init(context)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.top_bar, this)
        setListeners()
    }

    fun setTitle(title: String) {
        topBar_title.text = title.toUpperCase()
        topBarTitleChangeListener(title)
    }

    fun setProfileImage(profileImage : String?){
        Glide.with(context)
            .load(profileImage)
            .placeholder(R.drawable.user_image)
            .into(topBar_profileImage)
    }

    fun setTopBarListeners(topBarListener: TopBarListener){
        this.topBarListener = topBarListener
    }

    private fun setListeners(){

    }

    interface TopBarListener {
        fun onOptionsClick(isBackEnabled: Boolean)
    }

    fun setBackButtonEnabled(isEnabled: Boolean){
        topBar_optionsButton.setImageResource(if (isEnabled) R.drawable.ic_arrow_left else R.drawable.ic_options)
        topBar_optionsButton.setOnClickListener {
            topBarListener.onOptionsClick(isEnabled)
        }
    }
}


