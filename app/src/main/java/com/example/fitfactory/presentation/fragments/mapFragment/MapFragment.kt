package com.example.fitfactory.presentation.fragments.mapFragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.utils.Constants
import com.example.fitfactory.utils.PermissionRequestManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions

class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var viewModel: MapViewModel

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            map = it
            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.map_style
                )
            )

            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
            } else {
                PermissionRequestManager(activity as AppCompatActivity).sendRequest(Manifest.permission.ACCESS_FINE_LOCATION)
            }
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.759436, 19.456553), 13f))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        topBar?.setTitle("Mapa")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.LOCATION_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
            }
        } else {
            // Permission was denied. Display an error message.
        }
    }

}
