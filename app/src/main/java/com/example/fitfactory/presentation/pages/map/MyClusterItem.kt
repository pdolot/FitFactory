package com.example.fitfactory.presentation.pages.map

import com.example.fitfactory.data.models.FitnessClub
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class MyClusterItem(val fitnessClub: FitnessClub, val icon: BitmapDescriptor?, val id: Long?) : ClusterItem {

    override fun getSnippet(): String = ""

    override fun getTitle(): String = ""

    override fun getPosition(): LatLng = LatLng(fitnessClub.latitude ?: 0.0, fitnessClub.longitude ?: 0.0)

}