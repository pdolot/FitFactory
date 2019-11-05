package com.example.fitfactory.presentation.fragments.buyPass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.data.models.PassType
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_buy_pass.*

class BuyPassFragment : BaseFragment() {

    private lateinit var viewModel: BuyPassViewModel
    private lateinit var adapter: PassToBuyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BuyPassViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buy_pass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topBar?.setTitle(getString(R.string.buy_pass))
        flexibleLayout?.isViewEnable = false
        setPaddingTop(view)
        setAdapter()
        setListeners()
    }

    private fun setListeners() {
        buyPassFragment_buyButton.setOnClickListener {
            findNavController().navigate(R.id.paymentFragment)
        }
    }

    private fun setAdapter() {
        adapter = PassToBuyAdapter(PassType.values().toList())
        buyPassFragment_recyclerView.apply {
            this.adapter = this@BuyPassFragment.adapter
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        buyPassFragment_tabLayout.setupWithRecyclerView(buyPassFragment_recyclerView)
    }

    override fun backButtonEnabled(): Boolean = false
}
