package com.example.fitfactory.presentation.fragments.mapFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.android.synthetic.main.map_fragment.*

class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            map = it
            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.map_style))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.759436, 19.456553),13f))
        }
    }

    private lateinit var viewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.mapFragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        topBar?.setTitle("Mapa")
        topBar?.setOnClickListener {
            navController?.navigate(R.id.signInFragment2)
        }
    }


}
