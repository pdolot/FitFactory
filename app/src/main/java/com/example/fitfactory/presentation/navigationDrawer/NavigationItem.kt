package com.example.fitfactory.presentation.navigationDrawer

data class NavigationItem(
    var name: String? = null,
    var iconId: Int? = null,
    var destinationId: Int? = null,
    var topBarTitle: String? = null
)