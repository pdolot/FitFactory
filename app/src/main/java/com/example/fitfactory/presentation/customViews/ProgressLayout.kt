package com.example.fitfactory.presentation.customViews

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.fitfactory.R
import com.example.fitfactory.utils.animateDrawable
import com.example.fitfactory.utils.asAnimatedVectorDrawable
import com.example.fitfactory.utils.resetAnimation
import kotlinx.android.synthetic.main.progress_layout.view.*
import kotlin.math.roundToLong

class ProgressLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var viewWidth = 0
    private var tint = 0
    private var negativeTint = 0
    private var positiveTint = 0
    private var text: String? = null
    private var secondAnimType: SecondAnimType? = null

    private lateinit var secondAnimation: ObjectAnimator

    init {
        View.inflate(context, R.layout.progress_layout, this)
        val a =
            context.theme.obtainStyledAttributes(attrs, R.styleable.ProgressButton, defStyleAttr, 0)
        tint = a.getColor(
            R.styleable.ProgressButton_tint,
            ContextCompat.getColor(context, R.color.colorPrimaryDark)
        )
        negativeTint = a.getColor(
            R.styleable.ProgressButton_negativeTint,
            ContextCompat.getColor(context, R.color.colorAccent)
        )
        positiveTint = a.getColor(
            R.styleable.ProgressButton_positiveTint,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        text = a.getString(R.styleable.ProgressButton_text)

        secondAnimType =
            SecondAnimType.values()[a.getInt(R.styleable.ProgressButton_secondAnimType, 0)]

        title.text = text
        setTintColor(tint)

    }

    fun reset() {
        title.text = text
        setTintColor(tint)
    }

    private fun startSecondAnimation() {
        when (secondAnimType) {
            SecondAnimType.SEND -> sendAnimationEntry()
        }
    }

    private fun endSecondAnimation(): Long {
        return when (secondAnimType) {
            SecondAnimType.SEND -> sendAnimationExit()
            else -> 0
        }
    }

    private fun sendAnimationEntry() {
        secondAnim.visibility = View.VISIBLE
        secondAnim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.send_entry))
        secondAnim.drawable.asAnimatedVectorDrawable()?.setTint(tint)
        secondAnim.drawable.animateDrawable()

        secondAnimation = ObjectAnimator.ofFloat(secondAnim, "rotation", 0f, 360f).apply {
            repeatCount = ObjectAnimator.INFINITE
            startDelay = 1325
            duration = 3000
            interpolator = LinearInterpolator()
        }

        secondAnimation.start()
    }


    private fun sendAnimationExit(): Long {
        val endRotation = secondAnim.rotation
        var durationToFull = ((360f - endRotation) * 3000f / 360f).roundToLong()
        var isRunEndAnimation = false

        secondAnimation.end()

        secondAnimation = ObjectAnimator.ofFloat(secondAnim, "rotation", endRotation, 360f).apply {
            duration = durationToFull
            interpolator = LinearInterpolator()
            start()
        }

        secondAnimation = ObjectAnimator.ofFloat(secondAnim, "rotation", 0f, 180f).apply {
            startDelay = durationToFull
            duration = 1500
            interpolator = LinearInterpolator()
            addUpdateListener {
                if (it.animatedValue as Float > 90f) {
                    if (!isRunEndAnimation) {
                        secondAnim.drawable.animateDrawable()
                        isRunEndAnimation = true
                    }
                }
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    anim.drawable.animateDrawable()
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}

            })
        }

        secondAnimation.start()

        return durationToFull + 1500
    }

    private fun startRotate() {
        anim.animation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            duration = 2000
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
            start()
        }
    }

    fun onError(message: String) {
        anim.drawable.resetAnimation()
        anim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exit_anim))

        secondAnim.drawable.resetAnimation()
        secondAnim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.send_exit))

        setTintColor(negativeTint)
        title.text = message
    }

    fun onSuccess(message: String) {
        anim.drawable.resetAnimation()
        anim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exit_anim))

        secondAnim.drawable.resetAnimation()
        secondAnim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.send_exit))

        setTintColor(positiveTint)
        title.text = message
    }

    fun startAnim() {
        reset()
        changeClickableState(false)
        fadeTitle(0f)
        viewWidth = measuredWidth
        val widthAnimation = ValueAnimator.ofInt(measuredWidth, measuredHeight)
        widthAnimation.addUpdateListener { va ->
            val params = viewBackground.layoutParams
            params.width = va.animatedValue as Int
            viewBackground.layoutParams = params
        }

        widthAnimation.apply {
            startDelay = 1000
            duration = 1000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(p0: Animator?) {
                    anim.visibility = View.VISIBLE
                    viewBackground.visibility = View.GONE
                    anim.drawable.animateDrawable()
                    startRotate()
                    startSecondAnimation()
                }
            })
            start()
        }
    }

    fun stop() {

        val widthAnimation = ValueAnimator.ofInt(measuredHeight, viewWidth)
        widthAnimation.addUpdateListener { va ->
            val params = viewBackground.layoutParams
            params.width = va.animatedValue as Int
            viewBackground.layoutParams = params
        }

        val durationSecondEndAnimation = endSecondAnimation()

        widthAnimation.apply {
            startDelay = durationSecondEndAnimation + 2150
            duration = 1000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(p0: Animator?) {
                    anim.animation?.cancel()
                    anim.visibility = View.GONE
                    secondAnim.drawable.resetAnimation()
                    secondAnim.visibility = View.GONE
                    secondAnim.rotation = 0f
                    viewBackground.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    anim.drawable.resetAnimation()
                    anim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.enter_anim))
                    fadeTitle(255f)
                    changeClickableState(true)
                }
            })

            start()
        }
    }

    fun fadeTitle(toAlpha: Float) {
        title.animate().apply {
            alpha(toAlpha)
            duration = 1000
            start()
        }
    }

    private fun setTintColor(color: Int) {
        anim.drawable.asAnimatedVectorDrawable()?.setTint(color)
        secondAnim.drawable.asAnimatedVectorDrawable()?.setTint(color)
        viewBackground.drawable.setTint(color)
    }

    private fun changeClickableState(status: Boolean) {
        isClickable = status
        isEnabled = status
    }

    enum class SecondAnimType {
        NONE,
        SEND
    }
}