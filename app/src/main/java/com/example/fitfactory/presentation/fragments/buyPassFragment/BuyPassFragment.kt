package com.example.fitfactory.presentation.fragments.buyPassFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.fitfactory.R

class BuyPassFragment : Fragment() {

    companion object {
        fun newInstance() = BuyPassFragment()
    }

    private lateinit var viewModel: BuyPassViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.buy_pass_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BuyPassViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
