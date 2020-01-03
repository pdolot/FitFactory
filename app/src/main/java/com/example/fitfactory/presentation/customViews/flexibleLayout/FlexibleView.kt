package com.example.fitfactory.presentation.customViews.flexibleLayout

import android.content.Context
import android.graphics.Rect
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.fitfactory.R
import com.example.fitfactory.di.Injector
import com.example.fitfactory.utils.SpanTextUtil
import com.example.fitfactory.utils.generateQrCode
import com.google.zxing.qrcode.encoder.QRCode
import kotlinx.android.synthetic.main.flexible_layout.view.*
import javax.inject.Inject

class FlexibleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var activity: AppCompatActivity
    private var oldScreenBrightness = 0
    private var minTranslation: Float = 0f
    private var thumbRect = Rect()
    var isViewEnable: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                visibility = if (value) View.VISIBLE else View.GONE
            }
        }

    init {
        View.inflate(context, R.layout.flexible_layout, this)
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                flexibleLayout_content.setPadding(
                    0,
                    context.resources.getDimensionPixelSize(R.dimen.topBarHeight),
                    0,
                    0
                )
                flexibleLayout_thumb.getLocalVisibleRect(thumbRect)
                minTranslation =
                    (-flexibleLayout_content.measuredHeight + context.resources.getDimensionPixelSize(
                        R.dimen.topBarHeight
                    )).toFloat()
                translationY = minTranslation
            }
        })
        setListeners()
    }

    fun bindView(isLogged: Boolean){
        if (isLogged){
            notLoggedContent.visibility = View.GONE
        }else{
            notLoggedContent.visibility = View.VISIBLE
            qrCodeContent.visibility = View.GONE
            loggedWithoutPassContent.visibility = View.GONE
        }
    }

    fun setQrCode(qrCode: String?) {
        if (qrCode != null){
            qrCodeContent.visibility = View.VISIBLE
            loggedWithoutPassContent.visibility = View.GONE
            flexibleLayout_qrCode.generateQrCode(qrCode)
        }else{
            qrCodeContent.visibility = View.GONE
            loggedWithoutPassContent.visibility = View.VISIBLE
        }

//        Glide.with(context)
//            .load("https://pl.qr-code-generator.com/wp-content/themes/qr/new_structure/markets/core_market/generator/dist/generator/assets/images/websiteQRCode_noFrame.png")
//            .fitCenter()
//            .into(flexibleLayout_qrCode)
    }

    private fun setListeners() {
        flexibleLayout_thumb.setOnTouchListener { v, event ->
            v as FlexibleButton
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (event.x >= (measuredWidth / 2) - (v.thumbSize / 2) && event.x <= (measuredWidth / 2) + (v.thumbSize / 2)) {
                        v.touchStart = true
                        true
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (v.touchStart && event.y >= thumbRect.top + v.thumbSize + v.barWidth && event.y <= thumbRect.bottom - v.barWidth) {
                        v.touchY = event.y
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (v.touchStart && event.y >= thumbRect.top + v.thumbSize) {
                        v.touchY = v.thumbSize + v.barWidth
                        v.touchStart = false
                        translateAnimation()
                    }
                }
            }
            true
        }


        SpanTextUtil(context).setClickableSpanOnTextView(
            userNotLogged, "Zaloguj się", object : ClickableSpan(){
                override fun onClick(widget: View) {
                    navigate(R.id.signInFragment)
                }

            }, R.color.silverLight
        )

        SpanTextUtil(context).setClickableSpanOnTextView(
            passEmpty, "Kup karnet", object : ClickableSpan(){
                override fun onClick(widget: View) {
                    navigate(R.id.buyPassFragment)
                }

            }, R.color.silverLight
        )

    }

    private fun navigate(id: Int){
        activity.findNavController(R.id.main_host_fragment).navigate(id)
        forceClose()
    }

    fun getThumbHeight(): Int {
        return flexibleLayout_thumb.height
    }

    fun forceClose() {
        translationY = minTranslation
        flexibleLayout_thumb.changeIcon(minTranslation)
    }

    private fun translateAnimation() {
        var translation = if (translationY == 0f) {
            minTranslation
        } else {
            0f
        }
        animate().apply {
            translationY(translation)
            duration = 1000
            interpolator = LinearInterpolator()
            withEndAction {
                flexibleLayout_thumb.changeIcon(translationY)
            }
            start()
        }
    }
}