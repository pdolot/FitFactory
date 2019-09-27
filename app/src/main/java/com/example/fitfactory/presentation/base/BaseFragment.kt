package com.example.fitfactory.presentation.base

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.example.fitfactory.presentation.activities.mainActivity.MainFragmentInterface
import com.example.fitfactory.presentation.customViews.CustomDrawerLayout
import com.example.fitfactory.presentation.customViews.flexibleLayout.FlexibleView
import com.example.fitfactory.presentation.customViews.TopBar

abstract class BaseFragment : Fragment() {

    var actions: MainFragmentInterface? = null
    var topBar: TopBar? = null
    var drawerLayout: CustomDrawerLayout? = null
    var flexibleLayout: FlexibleView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            actions = context as? MainFragmentInterface
        } catch (e: Exception) {
            throw IllegalStateException("Main fragment must implement correct action interface")
        }
        topBar = actions?.getTopBar()
        drawerLayout = actions?.getDrawer()
        flexibleLayout = actions?.getFlexibleLayout()
    }

    override fun onDetach() {
        super.onDetach()
        actions = null
        topBar = null
        drawerLayout = null
        flexibleLayout = null
    }

    fun setPaddingTop(view: View) {
        val topBarHeight = topBar?.height ?: 0
        var thumbHeight = 0
        flexibleLayout?.let {
            thumbHeight = if (it.isViewEnable) {
                it.getThumbHeight().div(2)
            }else{
                0
            }
        }

        view.setPadding(
            view.paddingLeft,
            topBarHeight + thumbHeight + view.paddingTop,
            view.paddingRight,
            view.paddingBottom
        )
    }
}