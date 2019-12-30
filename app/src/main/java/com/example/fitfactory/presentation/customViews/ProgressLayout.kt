package com.example.fitfactory.presentation.customViews

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProgressLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var viewWidth = 0
    private var tint = 0
    private var negativeTint = 0
    private var positiveTint = 0
    private var text: String? = null
    private var secondAnimType: SecondAnimType? = null
    private var isResult: Boolean = false

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

        secondAnimType = SecondAnimType.values()[a.getInt(R.styleable.ProgressButton_secondAnimType, 0)]

        title.text = text
        setTintColor(tint)

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

    fun fadeTitle(toAlpha: Float){
        title.animate().apply {
            alpha(toAlpha)
            duration = 1000
            start()
        }
    }

    fun reset() {
        title.text = text
        setTintColor(tint)
    }

    private fun startSecondAnimation(){
        when(secondAnimType){
            SecondAnimType.SEND -> sendAnimationEntry()
        }
    }

    private fun endSecondAnimation(){
        when(secondAnimType){
            SecondAnimType.SEND -> sendAnimationExit()
        }
    }

    private fun sendAnimationEntry(){
        secondAnim.visibility = View.VISIBLE
        secondAnim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.send_entry))
        secondAnim.drawable.asAnimatedVectorDrawable()?.setTint(tint)
        secondAnim.drawable.animateDrawable()
        secondAnim.animation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
            start()
        }
    }


    private fun sendAnimationExit(){
        secondAnim.animation?.cancel()
        secondAnim.animation = RotateAnimation(
            0f,
            180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            fillAfter = true
            start()
        }

        secondAnim.drawable.resetAnimation()
        secondAnim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.send_exit))
        secondAnim.drawable.animateDrawable()


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
        isResult = true
        anim.drawable.resetAnimation()
        anim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exit_anim))
        anim.drawable.animateDrawable()
        setTintColor(negativeTint)
        title.text = message
    }

    fun onSuccess(message: String) {
        isResult = true
        anim.drawable.resetAnimation()
        anim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exit_anim))
        anim.drawable.animateDrawable()
        setTintColor(positiveTint)
        title.text = message
    }

    fun stop() {

        val widthAnimation = ValueAnimator.ofInt(measuredHeight, viewWidth)
        widthAnimation.addUpdateListener { va ->
            val params = viewBackground.layoutParams
            params.width = va.animatedValue as Int
            viewBackground.layoutParams = params
        }
        endSecondAnimation()
        widthAnimation.apply {
            startDelay = 2950
            duration = 1000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(p0: Animator?) {
                    anim.animation?.cancel()
                    anim.visibility = View.GONE
                    secondAnim.drawable.resetAnimation()
                    secondAnim.visibility = View.GONE
                    viewBackground.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    anim.drawable.resetAnimation()

                    anim.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.enter_anim))
                    fadeTitle(255f)
                    changeClickableState(true)
                    isResult = false
                }
            })

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

    enum class SecondAnimType{
        NONE,
        SEND
    }
}