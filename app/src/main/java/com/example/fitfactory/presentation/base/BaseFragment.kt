package com.example.fitfactory.presentation.base

import android.content.Context
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.fitfactory.presentation.activities.mainActivity.MainFragmentInterface
import com.example.fitfactory.presentation.customViews.CustomDrawerLayout
import com.example.fitfactory.presentation.customViews.TopBar
import com.example.fitfactory.presentation.customViews.flexibleLayout.FlexibleView

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
        view.setPadding(
            view.paddingLeft,
            view.paddingTop + (topBar?.height
                ?: 0) + (if (flexibleLayout?.isViewEnable == true) flexibleLayout?.getThumbHeight()
                ?: 0 else 0),
            view.paddingRight,
            view.paddingBottom
        )
    }

    override fun onResume() {
        super.onResume()
        backButtonEnabled().let {
            topBar?.setBackButtonEnabled(it)
            drawerLayout?.setDrawerLockMode(if (it) DrawerLayout.LOCK_MODE_LOCKED_CLOSED else DrawerLayout.LOCK_MODE_UNLOCKED)
        }

    }

    abstract fun backButtonEnabled(): Boolean
}