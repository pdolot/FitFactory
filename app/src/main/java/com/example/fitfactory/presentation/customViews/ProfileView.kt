package com.example.fitfactory.presentation.customViews

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.fitfactory.R
import com.sdsmdg.harjot.vectormaster.models.PathModel
import kotlinx.android.synthetic.main.profile_navigation_view.view.*
import kotlin.math.roundToInt

class ProfileView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var padding: Int = 50

    init {
        init(context)
    }

    fun setProfileImage(uri: Uri?) {
        Glide.with(context)
            .load(uri)
            .placeholder(R.drawable.user_image)
            .centerCrop()
            .into(profile_image)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.profile_navigation_view, this)
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                setParams(profile_image, profile_background.height - padding)
                setParams(profile_levelView, (profile_background.width * 0.2).roundToInt() - padding)
                setParams(profile_entriesCountView, (profile_background.width * 0.2).roundToInt() - padding)
            }
        })
    }

    private fun setParams(view: View, size: Int) {
        val params = view.layoutParams as LayoutParams
        params.width = size
        params.height = size
        if (view == profile_levelView) params.marginStart = padding / 2 + 4
        if (view == profile_entriesCountView) params.marginEnd = padding / 2 + 4
        view.layoutParams = params
    }



}