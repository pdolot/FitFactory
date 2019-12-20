package com.example.fitfactory.presentation.customViews.passPreviewDialog

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.Pass
import com.example.fitfactory.utils.generateQrCode
import kotlinx.android.synthetic.main.view_pass_preview_dialog.view.*

class PassPreview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var pass: Pass? = null
        set(value) {
            field = value
            bindData()
        }

    init {
        View.inflate(context, R.layout.view_pass_preview_dialog, this)
        bindData()
    }

    private fun bindData(){
        qrCode.generateQrCode(pass?.qrCode)
        passName.text = pass?.passType?.shortName
        duration.text = context.getString(R.string.duration, pass?.startDate, pass?.endDate)
        price.text = context.getString(R.string.pricePerMonth, pass?.passType?.periodPrice)
        boughtDate.text = pass?.boughtDate
        user.text = pass?.passUser?.firstName + " " + pass?.passUser?.lastName
        passDescription.text = pass?.passType?.description
        passBenefits.text = pass?.passType?.benefits?.joinToString(separator = "\n- ", prefix = "- ")

    }
}