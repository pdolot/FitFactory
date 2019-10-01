package com.example.fitfactory.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.fitfactory.R

class SpanTextUtil(private val context: Context) {

    fun setClickableSpanOnTextView(
        tv: TextView,
        span: String,
        clickableSpan: ClickableSpan,
        color: Int
    ) {
        if (tv.text.contains(span)) {
            val ss = SpannableString(tv.text)
            val si = span.toRegex().find(tv.text)?.range?.first
            val ei = span.toRegex().find(tv.text)?.range?.last
            if (si != null && ei != null) {
                ss.setSpan(object : ClickableSpan() {

                    override fun onClick(view: View) {
                        clickableSpan.onClick(view)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                        ds.color = ContextCompat.getColor(context, color)
                    }

                }, si, ei + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            tv.movementMethod = LinkMovementMethod.getInstance()
            tv.text = ss
        }
    }

    fun setSpanOnTextView(
        tv: TextView,
        span: String,
        color: Int
    ) {
        if (tv.text.contains(span)) {
            val ss = SpannableString(tv.text)
            val si = span.toRegex().find(tv.text)?.range?.first
            val ei = span.toRegex().find(tv.text)?.range?.last
            if (si != null && ei != null) {
                ss.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, color)),
                    si,
                    ei + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            tv.text = ss
        }
    }
}
