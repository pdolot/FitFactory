package com.example.fitfactory.presentation.fragments.mainFragment

import androidx.navigation.NavController
import com.example.fitfactory.presentation.components.TopBar

interface MainFragmentInterface {
    fun getTopBar(): TopBar?
    fun getNavController(): NavController?
}