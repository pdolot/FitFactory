package com.example.fitfactory.presentation.customViews

import android.animation.*
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitfactory.R
import com.example.fitfactory.data.models.FitnessClub
import com.example.fitfactory.utils.SpanTextUtil
import com.example.fitfactory.utils.isBetween
import kotlinx.android.synthetic.main.club_bar.view.*
import java.sql.Time
import java.util.*

class FloatingLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var isExpand: Boolean = false
    var isAnimated: Boolean = false
    private lateinit var floatingLayoutListener: FloatingLayoutListener
    private var animatorSet: AnimatorSet? = null
    private var gradientDrawable: GradientDrawable = GradientDrawable()
    private lateinit var cornerAnimation: ObjectAnimator
    private lateinit var widthAnimation: ValueAnimator
    private lateinit var biasAnimation: ValueAnimator

    var fitnessClub: FitnessClub? = null
        set(value) {
            field = value
            setLabels()
        }

    init {
        View.inflate(context, R.layout.club_bar, this)
        setListeners()
        gradientDrawable = clubBar_bottomBar.background as GradientDrawable
    }

    private fun setListeners() {
        clubBar_bottomBar.setOnClickListener {
            floatingLayoutListener.onClick()
        }
    }

    private fun setLabels() {
        fitnessClub?.let {
            clubBar_address.text = resources.getString(
                R.string.address,
                it.address?.city,
                it.address?.street,
                it.address?.zipCode,
                it.address?.zipCodeCity
            )
//        val calendar = Calendar.getInstance()

            clubBar_openHours.text = resources.getString(R.string.openHours, it.openHour, it.closeHour, "Otwarte")

            //            if (Time.valueOf("${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}").isBetween(
//                    fitnessClub.openHour ?: "00:00",
//                    fitnessClub.closeHour ?: "00:00"
//                )
//            ) "(Otwarte)" else "(Zamknięte)"

            clubBar_clutter.progress = (it.peopleCountAtThisTime.toFloat()/ it.peopleLimit.toFloat())
            clubBar_clutterIcon.drawable.setTint(clubBar_clutter.color)

            clubBar_name.text = fitnessClub?.name
        }
    }

    fun setDistance(distance: Float) {
        clubBar_distance.visibility = View.VISIBLE
        clubBar_distance.text = resources.getString(R.string.distance, distance)
        SpanTextUtil(context).setSpanOnTextView(
            clubBar_distance,
            String.format("%.2f", distance),
            R.color.primaryLight
        )
    }

    fun setFloatingLayoutListener(floatingLayoutListener: FloatingLayoutListener) {
        this.floatingLayoutListener = floatingLayoutListener
    }

    fun expand() {
        if (fitnessClub == null) return
        if (!isExpand) startEntryAnimation()
    }

    fun collapse() {
        if (fitnessClub == null) return
        if (isExpand) startExitAnimation()
    }

    fun toggle() {
        if (fitnessClub == null) return
        if (!isExpand) {
            startEntryAnimation()
        } else {
            collapseTopBar()
            startExitAnimation()
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        forceClose()
        return super.onSaveInstanceState()
    }

    fun forceClose(){
        clubBar_topBar.collapse(false)
        cornerAnimation = ObjectAnimator.ofFloat(gradientDrawable, "cornerRadius", 0f, 1000f).apply { start() }
        val params = clubBar_bottomBar.layoutParams as LayoutParams
        params.width = clubBar_bottomBar.height
        params.horizontalBias = 1f
        clubBar_bottomBar.layoutParams = params
        isExpand = false
        isAnimated = false
    }

    private fun startEntryAnimation() {

        val params = layoutParams
        params.width = LayoutParams.MATCH_PARENT
        layoutParams = params

        clubBar_bottomBar.post {
            cornerAnimation = ObjectAnimator.ofFloat(gradientDrawable, "cornerRadius", 1000f, 0f)
            widthAnimation = ValueAnimator.ofInt(clubBar_bottomBar.height, measuredWidth)
            biasAnimation = ValueAnimator.ofFloat(1f, 0.5f)

            biasAnimation.apply {
                duration = 1000
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                        isExpand = true
                        isAnimated = true
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


            animatorSet = AnimatorSet().apply {
                duration = 2000
                startDelay = 1000
                playTogether(cornerAnimation, widthAnimation)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        clubBar_bottomBar.isClickable = true
                        isAnimated = false
                        expandTopBar()
                    }
                })
                start()
            }

        }
    }

    private fun expandTopBar() {
        clubBar_topBar.expand(true)
//        clubBar_topBar.apply {
//            visibility = View.VISIBLE
//            translationY = clubBar_topBar.height.toFloat()
//            animate().translationY(0f).setDuration(1000).start()
//        }
    }

    private fun collapseTopBar() {
        clubBar_topBar.collapse(true)
//        clubBar_topBar.apply {
//            animate().translationY(clubBar_topBar.height.toFloat()).setDuration(1000)
//                .withEndAction {
//                    visibility = View.GONE
//                }.start()
//        }
    }

    private fun startExitAnimation() {
        cornerAnimation = ObjectAnimator.ofFloat(gradientDrawable, "cornerRadius", 0f, 1000f)
        widthAnimation = ValueAnimator.ofInt(measuredWidth, clubBar_bottomBar.height)
        biasAnimation = ValueAnimator.ofFloat(0.5f, 1f)

        widthAnimation.addUpdateListener { va ->
            val params = clubBar_bottomBar.layoutParams
            params.width = va.animatedValue as Int
            clubBar_bottomBar.layoutParams = params
        }

        animatorSet = AnimatorSet().apply {
            duration = 2000
            startDelay = 1000
            playTogether(cornerAnimation, widthAnimation)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    isExpand = false
                    isAnimated = true
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
                    clubBar_bottomBar.isClickable = true
                    isAnimated = false
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