package com.example.fitfactory.presentation.customViews.creditCard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.CreditCard
import kotlinx.android.synthetic.main.view_creadit_card.view.*

class CreditCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_creadit_card, this)
    }

    fun bindData(creditCard: CreditCard, user: String){
        cardNumber.text = creditCard.cardNumber
        cvcCvv.text = creditCard.cvcCvv
        expiryDate.text = creditCard.expiryDate
        owner.text = user
    }
}