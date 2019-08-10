package com.example.fitfactory.presentation.base

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.fitfactory.presentation.components.TopBar
import com.example.fitfactory.presentation.fragments.mainFragment.MainFragmentInterface

abstract class BaseFragment : Fragment() {

    var actions: MainFragmentInterface? = null
    var navController: NavController? = null
    var topBar: TopBar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            actions = context as? MainFragmentInterface
        } catch (e: Exception) {
            throw IllegalStateException("Main fragment must implement correct action interface")
        }
        topBar = actions?.getTopBar()
        navController = actions?.getNavController()
        topBar?.visibility = View.VISIBLE
    }

    override fun onDetach() {
        super.onDetach()
        topBar?.visibility = View.GONE
        actions = null
        navController = null
        topBar = null
    }
}