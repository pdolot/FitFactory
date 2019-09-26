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

    fun setLevel(entriesCount: Int) {
        val first: PathModel? = profile_level.getPathModelByName("firstFront")
        val second: PathModel? = profile_level.getPathModelByName("secondFront")
        val third: PathModel? = profile_level.getPathModelByName("thirdFront")
        val fourth: PathModel? = profile_level.getPathModelByName("fourthFront")

        val level: Int = getLevel(entriesCount)

        first?.trimPathEnd = 0f
        second?.trimPathEnd = 0f
        third?.trimPathEnd = 0f
        fourth?.trimPathEnd = 0f

        if (level == 8) {
            first?.trimPathEnd = 1f
            second?.trimPathEnd = 1f
            third?.trimPathEnd = 1f
            fourth?.trimPathEnd = 1f
        } else {
            when (val c = entriesCount - (level * 30)) {
                in 0..10 -> first?.let {
                    it.trimPathEnd = 0.1f * c
                }
                in 11..15 -> {
                    first?.let {
                        it.trimPathEnd = 1f
                    }

                    second?.let {
                        it.trimPathEnd = 0.2f * (c - 10)
                    }
                }
                in 16..20 -> {
                    first?.let {
                        it.trimPathEnd = 1f
                    }

                    second?.let {
                        it.trimPathEnd = 1f
                    }

                    third?.let {
                        it.trimPathEnd = 0.2f * (c - 15)
                    }
                }
                in 21..30 -> {
                    first?.let {
                        it.trimPathEnd = 1f
                    }

                    second?.let {
                        it.trimPathEnd = 1f
                    }

                    third?.let {
                        it.trimPathEnd = 1f
                    }

                    fourth?.let {
                        it.trimPathEnd = 0.1f * (c - 20)
                    }
                }

            }
        }

        val levelColorSet = getLevelColors(level)

        first?.strokeColor = levelColorSet[0]
        second?.strokeColor = levelColorSet[1]
        third?.strokeColor = levelColorSet[1]
        fourth?.strokeColor = levelColorSet[2]

        profile_level.update()
    }

    private fun getLevelColors(level: Int): ArrayList<Int> {
        var levelColorSet = ArrayList<Int>()

        when (level) {
            0 -> {
                levelColorSet.add(Color.parseColor("#B53636"))
                levelColorSet.add(Color.parseColor("#CC8322"))
                levelColorSet.add(Color.parseColor("#F7C745"))
            }
            1 -> {
                levelColorSet.add(Color.parseColor("#F7C745"))
                levelColorSet.add(Color.parseColor("#6ABF45"))
                levelColorSet.add(Color.parseColor("#367A40"))
            }
            2 -> {
                levelColorSet.add(Color.parseColor("#367A40"))
                levelColorSet.add(Color.parseColor("#09A880"))
                levelColorSet.add(Color.parseColor("#2AD4D4"))
            }
            3 -> {
                levelColorSet.add(Color.parseColor("#2AD4D4"))
                levelColorSet.add(Color.parseColor("#259DB8"))
                levelColorSet.add(Color.parseColor("#1D67C2"))
            }
            4 -> {
                levelColorSet.add(Color.parseColor("#1D67C2"))
                levelColorSet.add(Color.parseColor("#2F33AD"))
                levelColorSet.add(Color.parseColor("#662FA1"))
            }
            5 -> {
                levelColorSet.add(Color.parseColor("#662FA1"))
                levelColorSet.add(Color.parseColor("#902FA1"))
                levelColorSet.add(Color.parseColor("#C92B5A"))
            }
            6 -> {
                levelColorSet.add(Color.parseColor("#C92B5A"))
                levelColorSet.add(Color.parseColor("#AB283B"))
                levelColorSet.add(Color.parseColor("#c79a00"))
            }
            7, 8 -> {
                levelColorSet.add(Color.parseColor("#c79a00"))
                levelColorSet.add(Color.parseColor("#ffca28"))
                levelColorSet.add(Color.parseColor("#fffd61"))
            }
        }

        return levelColorSet
    }

    private fun getLevel(entriesCount: Int): Int {
        when (entriesCount) {
            in 0..30 -> return 0
            in 31..60 -> return 1
            in 61..90 -> return 2
            in 91..120 -> return 3
            in 121..150 -> return 4
            in 151..180 -> return 5
            in 181..210 -> return 6
            in 211..240 -> return 7
        }

        return 8
    }

}