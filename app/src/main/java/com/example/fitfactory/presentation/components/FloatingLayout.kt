package com.example.fitfactory.presentation.components

import android.animation.*
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitfactory.R
import kotlinx.android.synthetic.main.club_bar.view.*

class FloatingLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var isExpand: Boolean = false
    private lateinit var floatingLayoutListener: FloatingLayoutListener

    init {
        init(context)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.club_bar, this)
        setListeners()
    }

    private fun setListeners() {
        clubBar_bottomBar.setOnClickListener {
            floatingLayoutListener.onClick()
        }
    }

    fun setFloatingLayoutListener(floatingLayoutListener: FloatingLayoutListener) {
        this.floatingLayoutListener = floatingLayoutListener
    }

    fun expand() {
        if (!isExpand) startEntryAnimation()
    }

    fun collapse() {
        if (isExpand) startExitAnimation()
    }

    fun toggle() {
        if (!isExpand) {
            startEntryAnimation()
        } else {
            collapseTopBar()
            startExitAnimation()
        }
    }


    private fun startEntryAnimation() {

        val params = layoutParams
        params.width = LayoutParams.MATCH_PARENT
        layoutParams = params

        clubBar_bottomBar.post {
            val gradientDrawable: GradientDrawable = clubBar_bottomBar.background as GradientDrawable
            val cornerAnimation: ObjectAnimator = ObjectAnimator.ofFloat(gradientDrawable, "cornerRadius", 1000f, 0f)
            val widthAnimation: ValueAnimator = ValueAnimator.ofInt(clubBar_bottomBar.height, measuredWidth)
            val biasAnimation: ValueAnimator = ValueAnimator.ofFloat(1f, 0.5f)

            biasAnimation.apply {
                duration = 1000
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                        clubBar_bottomBar.isClickable = false
                    }
                })
                addUpdateListener { va ->
                    val params = clubBar_bottomBar.layoutParams as LayoutParams
                    params.horizontalBias = va.animatedValue as Float
                    clubBar_bottomBar.layoutParams = params
                }
                start()
            }

            widthAnimation.addUpdateListener { va ->
                val params = clubBar_bottomBar.layoutParams
                params.width = va.animatedValue as Int
                clubBar_bottomBar.layoutParams = params
            }


            AnimatorSet().apply {
                duration = 2000
                startDelay = 1000
                playTogether(cornerAnimation, widthAnimation)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        isExpand = true
                        clubBar_bottomBar.isClickable = true
                        expandTopBar()
                    }
                })
                start()
            }
        }
    }

    private fun expandTopBar() {
        clubBar_topBar.apply {
            visibility = View.VISIBLE
            translationY = clubBar_topBar.height.toFloat()
            animate().translationY(0f).setDuration(1000).start()
        }
    }

    private fun collapseTopBar() {
        clubBar_topBar.apply {
            animate().translationY(clubBar_topBar.height.toFloat()).setDuration(1000).withEndAction {
                visibility = View.INVISIBLE
            }.start()
        }
    }

    private fun startExitAnimation() {
        val gradientDrawable: GradientDrawable = clubBar_bottomBar.background as GradientDrawable
        val cornerAnimation: ObjectAnimator = ObjectAnimator.ofFloat(gradientDrawable, "cornerRadius", 0f, 1000f)
        val widthAnimation: ValueAnimator = ValueAnimator.ofInt(measuredWidth, clubBar_bottomBar.height)
        val biasAnimation: ValueAnimator = ValueAnimator.ofFloat(0.5f, 1f)

        widthAnimation.addUpdateListener { va ->
            val params = clubBar_bottomBar.layoutParams
            params.width = va.animatedValue as Int
            clubBar_bottomBar.layoutParams = params
        }

        AnimatorSet().apply {
            duration = 2000
            startDelay = 1000
            playTogether(cornerAnimation, widthAnimation)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    clubBar_bottomBar.isClickable = false
                }
            })
            start()
        }

        biasAnimation.apply {
            addUpdateListener { va ->
                val params = clubBar_bottomBar.layoutParams as LayoutParams
                params.horizontalBias = va.animatedValue as Float
                clubBar_bottomBar.layoutParams = params
            }

            duration = 1000
            startDelay = 3000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    isExpand = false
                    clubBar_bottomBar.isClickable = true
                    val params = layoutParams
                    params.width = LayoutParams.WRAP_CONTENT
                    layoutParams = params
                }
            })
            start()
        }
    }

    interface FloatingLayoutListener {
        fun onClick()
    }
}