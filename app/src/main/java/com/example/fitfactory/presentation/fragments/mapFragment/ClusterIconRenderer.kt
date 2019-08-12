package com.example.fitfactory.presentation.fragments.mapFragment

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class ClusterIconRenderer(context: Context?, map: GoogleMap?, clusterManager: ClusterManager<MyClusterItem>?) :
    DefaultClusterRenderer<MyClusterItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: MyClusterItem?, markerOptions: MarkerOptions?) {
        markerOptions?.apply {
            item?.let {
                icon(it.icon)
                snippet(it.snippet)
                title(it.title)
            }
        }
        super.onBeforeClusterItemRendered(item, markerOptions)
    }
}