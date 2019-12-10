package com.example.fitfactory.presentation.pages.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.utils.addMaskAndTextWatcher
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardNo.addMaskAndTextWatcher("#### #### #### ####")
        cardExpiry.addMaskAndTextWatcher("##/##")
        userBirthDate.addMaskAndTextWatcher("##/##/####")
        userPhoneNo.addMaskAndTextWatcher("### ### ###")
        userZipCode.addMaskAndTextWatcher("##-###")

        paymentFragment_payButton.setButtonClickListener {isClickable ->
            if (isClickable){
                Toast.makeText(context,"PAY", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Sprawdź poprawność danych", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.payment)
    override fun topBarEnabled() = true
    override fun backButtonEnabled(): Boolean = true
}