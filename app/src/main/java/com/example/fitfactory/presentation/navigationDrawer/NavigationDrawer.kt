package com.example.fitfactory.presentation.navigationDrawer

import android.content.Context
import android.net.Uri
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fitfactory.R
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.utils.LevelUtil
import com.example.fitfactory.utils.SpanTextUtil
import com.example.fitfactory.utils.animateDrawable
import com.example.fitfactory.utils.resetAnimation
import kotlinx.android.synthetic.main.navigation_layout.view.*
import kotlinx.android.synthetic.main.navigation_layout.view.profile_image
import kotlinx.android.synthetic.main.profile_navigation_view.view.*
import javax.inject.Inject

class NavigationDrawer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var localStorage: LocalStorage

    var onSignInClick: () -> Unit = {}
    var onSignUpClick: () -> Unit = {}

    init {
        View.inflate(context, R.layout.navigation_layout, this)
        Injector.component.inject(this)
        SpanTextUtil(context).apply {
            setClickableSpanOnTextView(
                actionLabel,
                context.getString(R.string.signUpSpan),
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        onSignUpClick()
                    }
                },
                R.color.silverLight
            )
            setClickableSpanOnTextView(
                actionLabel,
                context.getString(R.string.signInSpan),
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        onSignInClick()
                    }
                },
                R.color.silverLight
            )
        }
    }


    fun setProfileView() {
        localStorage.getUser()?.let {
            Glide.with(context)
                .load(it.profileImage)
                .placeholder(R.drawable.user_image)
                .fitCenter()
                .into(profile_image)
            
            if (it.firstName != null && it.lastName != null) {
                navigation_userName.text = "${it.firstName} ${it.lastName}"
            } else {
                navigation_userName.text = "${it.username}"
            }

            navigation_entries.text = it.entriesCount.toString()
            LevelUtil.Companion.Builder()
                .setView(navigation_profileLevel)
                .build()
                .setLevel(it.entriesCount)
        }
    }

    fun resetScroll(){
        navigationScroll.scrollTo(0,0)
    }

    fun setAdapter(navigationAdapter: NavigationRecyclerViewAdapter) {
        navigation_recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = navigationAdapter
        }
    }

    fun setHeader(isLogged: Boolean) {
        headerIfLogged.visibility = if (isLogged) View.VISIBLE else View.GONE
        headerIfNotLogged.visibility = if (isLogged) View.GONE else View.VISIBLE
        animatedCircle.drawable.apply {
            if (!isLogged) {
                this.animateDrawable()
            } else {
                this.resetAnimation()
            }
        }
    }
}