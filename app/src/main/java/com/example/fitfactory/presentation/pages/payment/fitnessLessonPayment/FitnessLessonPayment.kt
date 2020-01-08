package com.example.fitfactory.presentation.pages.payment.fitnessLessonPayment

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.CreditCard
import com.example.fitfactory.data.models.app.StateComplete
import com.example.fitfactory.data.models.app.StateError
import com.example.fitfactory.data.models.app.StateInProgress
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.utils.addMaskAndTextWatcher
import kotlinx.android.synthetic.main.fragment_fitness_lesson_payment.*
import kotlinx.android.synthetic.main.fragment_fitness_lesson_payment.cardCvc
import kotlinx.android.synthetic.main.fragment_fitness_lesson_payment.cardExpiry
import kotlinx.android.synthetic.main.fragment_fitness_lesson_payment.cardNo
import kotlinx.android.synthetic.main.fragment_fitness_lesson_payment.payButton
import kotlinx.android.synthetic.main.fragment_fitness_lesson_payment.price

class FitnessLessonPayment : BaseFragment() {

    private val viewModel by lazy { FitnessLessonPaymentViewModel() }
    private val args: FitnessLessonPaymentArgs by navArgs()
    private val textWatchers = ArrayList<Pair<TextWatcher, EditText>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fitness_lesson_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addValidators(this)

        viewModel.creditCardDao.getCreditCard(viewModel.localStorage.getUser()?.id ?: return)
            .observe(viewLifecycleOwner, Observer {
                bindCreditCard(it)
            })

        viewModel.validateResult.observe(viewLifecycleOwner, Observer {
            payButton.isEnable = it
        })

        viewModel.callState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is StateInProgress -> {
                    payButton.isEnable = false
                    viewModel.isJobPaused = true
                }
                is StateError -> {
                    payButton.isEnable = true
                    viewModel.isJobPaused = false
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is StateComplete -> {
                    payButton.isEnable = true
                    viewModel.isJobPaused = false
                    findNavController().navigate(R.id.yourActivity)
                    Toast.makeText(context, "Zapłacono", Toast.LENGTH_SHORT).show()
                }
            }
        })

        price.text =  getString(R.string.price, args.price)
        fitnessLesson_name.text = args.fitnessLessonName
        fitnessLesson_date.text = args.date

        payButton.setButtonClickListener { isClickable ->
            if (isClickable && viewModel.validate()) {
                viewModel.createToken(cardExpiry.text.toString().trim(), cardCvc.text.toString().trim(), args.entryId, args.price.toDouble())
            } else {
                Toast.makeText(context, "Sprawdź poprawność danych", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        textWatchers.forEach {
            it.second.removeTextChangedListener(it.first)
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        textWatchers.add(Pair(cardNo.addMaskAndTextWatcher("#### #### #### ####"), cardNo))
        textWatchers.add(Pair(cardExpiry.addMaskAndTextWatcher("##/##"), cardExpiry))
    }

    private fun bindCreditCard(creditCard: CreditCard?) {
        creditCard?.let {
            cardNo.setText(it.cardNumber)
            cardExpiry.setText(it.expiryDate)
            cardCvc.setText(it.cvcCvv)
        }
    }

    override fun onDestroy() {
        textWatchers.forEach {
            it.second.removeTextChangedListener(it.first)
        }
        viewModel.job.cancel()
        super.onDestroy()
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.payment)
    override fun topBarEnabled() = true
    override fun backButtonEnabled(): Boolean = true
}