package com.example.fitfactory.presentation.activities.mainActivity

import com.example.fitfactory.presentation.customViews.CustomDrawerLayout
import com.example.fitfactory.presentation.customViews.flexibleLayout.FlexibleView
import com.example.fitfactory.presentation.customViews.TopBar

interface MainFragmentInterface {
    fun getTopBar(): TopBar?
    fun getDrawer(): CustomDrawerLayout?
    fun getFlexibleLayout(): FlexibleView
}