package com.example.fitfactory.presentation.pages.payment

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.utils.TimeUtil
import com.example.fitfactory.utils.addMaskAndTextWatcher
import kotlinx.android.synthetic.main.fragment_payment.*
import org.joda.time.DateTime

class PaymentFragment : BaseFragment() {

    private val viewModel by lazy { PaymentViewModel() }
    private val args: PaymentFragmentArgs by navArgs()
    private val textWatchers = ArrayList<Pair<TextWatcher, EditText>>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTheme(R.style.PayTheme)
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.passId = args.passId
        viewModel.addValidators(this)

        bindData()

        viewModel.validateResult.observe(viewLifecycleOwner, Observer {
            paymentFragment_payButton.isEnable = it
        })

        paymentFragment_payButton.setButtonClickListener { isClickable ->
            if (isClickable) {
                Toast.makeText(context, "PAY", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Sprawdź poprawność danych", Toast.LENGTH_SHORT).show()
            }
        }

        paymentFragment_editExpiryDate.setOnClickListener {
            showDatePickerDialog()
        }

        viewModel.passType.observe(viewLifecycleOwner, Observer {
            it?.let {
                paymentFragment_passName.text = it.shortName
                if (it.durationInDays ?: 0 > 30) {
                    paymentFragment_passPrice.text =
                        getString(R.string.pricePerMonth, it.periodPrice) + "*"
                } else {
                    paymentFragment_passPrice.text = it.periodPrice.toString() + " zł"
                }
                setExpiryDate(DateTime.now())
            }
        })

    }

    override fun onResume() {
        super.onResume()
        textWatchers.add(Pair(cardNo.addMaskAndTextWatcher("#### #### #### ####"), cardNo))
        textWatchers.add(Pair(cardExpiry.addMaskAndTextWatcher("##/##"), cardExpiry))
        textWatchers.add(Pair(userBirthDate.addMaskAndTextWatcher("##/##/####"), userBirthDate))
        textWatchers.add(Pair(userPhoneNo.addMaskAndTextWatcher("### ### ###"), userPhoneNo))
        textWatchers.add(Pair(userZipCode.addMaskAndTextWatcher("##-###"), userZipCode))
    }

    private fun showDatePickerDialog() {
        MaterialDialog(activity ?: return).show {
            datePicker(requireFutureDate = true, currentDate = viewModel.date.toGregorianCalendar()) { dialog, datetime ->
                setExpiryDate(DateTime(datetime.time))
            }
        }
    }

    fun setExpiryDate(date: DateTime){
        viewModel.date = date
        paymentFragment_passExpiry.text = getString(
            R.string.duration,
            TimeUtil.getDateAsString(date, "dd/MM/yyyy"),
            TimeUtil.getDateAsString(
                date.plusDays(viewModel.passType.value?.durationInDays ?: 0),
                "dd/MM/yyyy"
            )
        )
    }

    private fun bindData() {
        val user = viewModel.localStorage.getUser()
        user?.let {
            firstName.setText(user.firstName)
            lastName.setText(user.lastName)
            userEmail.setText(user.email)
            userPersonalIdentity.setText(user.identityNumber)
            userBirthDate.setText(user.birthDate)
            userPhoneNo.setText(user.phoneNumber)
            userAddressStreet.setText(user.address?.street)
            userAddressCity.setText(user.address?.city)
            userZipCode.setText(user.address?.zipCode)
            userZipCodeCity.setText(user.address?.zipCodeCity)
        }
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.payment)
    override fun topBarEnabled() = true
    override fun backButtonEnabled(): Boolean = true

    override fun onDestroy() {
        textWatchers.forEach {
            it.second.removeTextChangedListener(it.first)
        }
        activity?.setTheme(R.style.AppTheme)
        viewModel.job.cancel()
        super.onDestroy()
    }
}