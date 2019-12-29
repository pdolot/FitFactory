package com.example.fitfactory.presentation.pages.payment.contractTerminationPayment

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
import kotlinx.android.synthetic.main.fragment_penalty_payment.*

class ContractTermination : BaseFragment() {

    private val viewModel by lazy { ContractTerminationViewModel() }
    private val args: ContractTerminationArgs by navArgs()
    private val textWatchers = ArrayList<Pair<TextWatcher, EditText>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_penalty_payment, container, false)
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
                    findNavController().popBackStack()
                    Toast.makeText(context, "Umowa zerwana", Toast.LENGTH_SHORT).show()
                }
            }
        })


        price.text =  getString(R.string.price, args.penalty)
        info.text = getString(R.string.contractTerminationInfo, args.passName)

        payButton.setButtonClickListener { isClickable ->
            if (isClickable && viewModel.validate()) {
                viewModel.terminateContract(args.passId)
            } else {
                Toast.makeText(context, "Sprawdź poprawność danych", Toast.LENGTH_SHORT).show()
            }
        }
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