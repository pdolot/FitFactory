package com.example.fitfactory.presentation.pages.buyPass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.presentation.pages.payment.passPayment.PaymentFragmentDirections
import kotlinx.android.synthetic.main.fragment_buy_pass.*

class BuyPassFragment : BaseFragment() {

    private val viewModel by lazy { BuyPassViewModel() }
    private val adapter by lazy { PassToBuyAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buy_pass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setListeners()
        buyPassFragment_tabLayout.setupWithRecyclerView(buyPassFragment_recyclerView)

        viewModel.getPassesType()

        buyPassFragment_buyButton.isEnabled = viewModel.localStorage.isLogged()

        viewModel.passes.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })


    }

    private fun setListeners() {
        buyPassFragment_buyButton.setOnClickListener {
            if (viewModel.localStorage.getUser()?.identityNumber.isNullOrBlank()) {
                MaterialDialog(activity ?: return@setOnClickListener).show {
                    title(text = "Uzupełnij profil")
                    message(text = "Aby kupić bilet musisz uzpełnić swoje dane. Czy chcesz uzupełnić profil?")
                    positiveButton(text = "Uzupełnij") {
                        findNavController().navigate(R.id.editProfile)
                    }
                    negativeButton(text = "Anuluj")
                }
            } else {
                adapter.getPassTypeId()?.let {
                    findNavController().navigate(PaymentFragmentDirections.toPaymentFragment(it))
                }
            }

        }
    }

    private fun setAdapter() {
        buyPassFragment_recyclerView.apply {
            this.adapter = this@BuyPassFragment.adapter
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.buy_pass)
    override fun topBarEnabled() = true
    override fun backButtonEnabled(): Boolean = false
}
