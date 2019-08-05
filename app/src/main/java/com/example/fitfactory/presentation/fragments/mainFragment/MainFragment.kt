package com.example.fitfactory.presentation.fragments.mainFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment

class MainFragment : BaseFragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // need to check if user is logged
        findNavController().navigate(R.id.action_mainFragment_to_signInFragment)
    }
}
