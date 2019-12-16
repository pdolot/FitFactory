package com.example.fitfactory.presentation.customViews.creditCard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.CreditCard
import com.example.fitfactory.utils.addMaskAndTextWatcher
import kotlinx.android.synthetic.main.view_credit_card_dialog.view.*

class CreditCardDialog @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var creditCard: CreditCard? = null
        set(value) {
            field = value
            bindData(field)
        }

    init {
        View.inflate(context, R.layout.view_credit_card_dialog, this)
    }

    fun getCard(): CreditCard? {
        val number = cardNo.text.toString()
        val expiryDate = cardExpiry.text.toString()
        val cvcCvv = cardCvc.text.toString()

        if (number.isBlank() || number.isNullOrEmpty()) {
            cardNo.error = "Pole nie może być puste"
            return null
        }

        if (expiryDate.isBlank() || expiryDate.isNullOrEmpty()) {
            cardExpiry.error = "Pole nie może być puste"
            return null
        }

        if (cvcCvv.isBlank() || cvcCvv.isNullOrEmpty()) {
            cardCvc.error = "Pole nie może być puste"
            return null
        }

        return CreditCard(
            cardNumber = number,
            expiryDate = expiryDate,
            cvcCvv = cvcCvv
        )
    }

    private fun bindData(creditCard: CreditCard?) {
        cardNo.setText(creditCard?.cardNumber)
        cardExpiry.setText(creditCard?.expiryDate)
        cardCvc.setText(creditCard?.cvcCvv)

        cardNo.addMaskAndTextWatcher("#### #### #### ####")
        cardExpiry.addMaskAndTextWatcher("##/##")
    }

}