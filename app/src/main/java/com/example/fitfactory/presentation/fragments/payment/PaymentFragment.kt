package com.example.fitfactory.presentation.fragments.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.utils.addMaskAndTextWatcher
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.coroutines.*

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
        topBar?.setTitle(getString(R.string.payment))
        flexibleLayout?.isViewEnable = false
        setPaddingTop(view)

        paymentFragment_cardNo.addMaskAndTextWatcher("#### #### #### ####")
        paymentFragment_cardExpiry.addMaskAndTextWatcher("##/##")
        paymentFragment_userBirthDate.addMaskAndTextWatcher("##/##/####")
        paymentFragment_userPhoneNo.addMaskAndTextWatcher("### ### ###")
        paymentFragment_userZipCode.addMaskAndTextWatcher("##-###")

        paymentFragment_payButton.setButtonClickListener {isClickable ->
            if (isClickable){
                Toast.makeText(context,"PAY", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Sprawdź poprawność danych", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun backButtonEnabled(): Boolean = true
}