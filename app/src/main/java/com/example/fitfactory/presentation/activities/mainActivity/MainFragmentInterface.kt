package com.example.fitfactory.presentation.activities.mainActivity

import com.example.fitfactory.presentation.components.CustomDrawerLayout
import com.example.fitfactory.presentation.components.FlexibleBottomView
import com.example.fitfactory.presentation.components.TopBar

interface MainFragmentInterface {
    fun getTopBar(): TopBar?
    fun getDrawer(): CustomDrawerLayout?
    fun getFlexibleLayout(): FlexibleBottomView
}