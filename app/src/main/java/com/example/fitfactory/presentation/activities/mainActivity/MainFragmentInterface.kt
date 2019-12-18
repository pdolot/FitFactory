package com.example.fitfactory.presentation.activities.mainActivity

import com.example.fitfactory.presentation.customViews.CustomDrawerLayout
import com.example.fitfactory.presentation.customViews.flexibleLayout.FlexibleView
import com.example.fitfactory.presentation.customViews.TopBar
import com.example.fitfactory.presentation.navigationDrawer.NavigationDrawer

interface MainFragmentInterface {
    fun getTopBar(): TopBar?
    fun getDrawer(): CustomDrawerLayout?
    fun getFlexibleLayout(): FlexibleView
    fun getNavigationDrawer(): NavigationDrawer
}