package com.example.fitfactory.presentation.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.fitfactory.presentation.activities.mainActivity.MainFragmentInterface
import com.example.fitfactory.presentation.components.TopBar

abstract class BaseFragment : Fragment() {

    var actions: MainFragmentInterface? = null
    var topBar: TopBar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            actions = context as? MainFragmentInterface
        } catch (e: Exception) {
            throw IllegalStateException("Main fragment must implement correct action interface")
        }
        topBar = actions?.getTopBar()
    }

    override fun onDetach() {
        super.onDetach()
        actions = null
        topBar = null
    }
}