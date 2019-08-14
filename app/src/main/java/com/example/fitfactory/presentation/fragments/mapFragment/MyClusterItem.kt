package com.example.fitfactory.presentation.fragments.mapFragment

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class MyClusterItem(private val latLng: LatLng, val icon: BitmapDescriptor?, val id: Long?) : ClusterItem {

    override fun getSnippet(): String = ""

    override fun getTitle(): String = ""

    override fun getPosition(): LatLng = latLng

}