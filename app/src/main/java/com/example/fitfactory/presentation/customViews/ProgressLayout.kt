package com.example.fitfactory.presentation.customViews

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.fitfactory.R
import com.example.fitfactory.utils.animateDrawable
import com.example.fitfactory.utils.resetAnimation
import kotlinx.android.synthetic.main.progress_layout.view.*

class ProgressLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var onClick: () -> Unit = {}
    var viewWidth = 0
    var isEnable = false

    init {
        View.inflate(context, R.layout.progress_layout, this)
        setOnClickListener {
            if (!isEnable) startEntryAnimation() else startExitAnimation()

        }
    }

    private fun startEntryAnimation() {
        isEnable = true
        viewWidth = measuredWidth
        val widthAnimation = ValueAnimator.ofInt(measuredWidth, measuredHeight)
        widthAnimation.addUpdateListener { va ->
            val params = viewBackground.layoutParams
            params.width = va.animatedValue as Int
            viewBackground.layoutParams = params
        }

        widthAnimation.apply {
            duration = 1000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(p0: Animator?) {
                    viewBackground.visibility = View.GONE
                    anim.drawable.animateDrawable()
                }

            })
            start()
        }
    }

    private fun startExitAnimation() {
        anim.drawable.resetAnimation()
        anim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exit_anim))
        anim.drawable.animateDrawable()
        val widthAnimation = ValueAnimator.ofInt(measuredHeight, viewWidth)
        widthAnimation.addUpdateListener { va ->
            val params = viewBackground.layoutParams
            params.width = va.animatedValue as Int
            viewBackground.layoutParams = params
        }

        widthAnimation.apply {
            duration = 1000
            startDelay = 3000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(p0: Animator?) {
                    viewBackground.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    anim.drawable.resetAnimation()
                    anim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.enter_anim))
                    isEnable = false
                }
            })

            start()
        }
    }
}