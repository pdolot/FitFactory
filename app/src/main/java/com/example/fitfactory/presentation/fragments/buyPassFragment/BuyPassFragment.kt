package com.example.fitfactory.presentation.fragments.buyPassFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_buy_pass.*

class BuyPassFragment : BaseFragment() {

    private lateinit var viewModel: BuyPassViewModel
    private lateinit var adapter: PassAdapter

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
        flexibleLayout?.setViewEnabled(false)
        setPaddingTop(view)
        setAdapter()
    }

    private fun setAdapter() {
        adapter = PassAdapter(listOf(
            Pass(name = "ProAge1"),
            Pass(name = "ProAge2"),
            Pass(name = "ProAge3"),
            Pass(name = "ProAge4")))
        buyPassFragment_recyclerView.apply {
            this.adapter = this@BuyPassFragment.adapter
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        buyPassFragment_tabLayout.setupWithRecyclerView(buyPassFragment_recyclerView)
    }

    override fun onDestroyView() {
        flexibleLayout?.setViewEnabled(true)
        super.onDestroyView()
    }

}
