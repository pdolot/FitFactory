package com.example.fitfactory.presentation.pages.buyPass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_buy_pass.*

class BuyPassFragment : BaseFragment() {

    private val viewModel by lazy {BuyPassViewModel()  }
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

        viewModel.getPassesType()
        buyPassFragment_buyButton.isEnabled = viewModel.localStorage.isLogged()
        viewModel.passes.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            buyPassFragment_tabLayout.setupWithRecyclerView(buyPassFragment_recyclerView)
        })
    }

    private fun setListeners() {
        buyPassFragment_buyButton.setOnClickListener {
            findNavController().navigate(R.id.paymentFragment)
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
