package com.example.fitfactory.presentation.fragments.mapFragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.fitfactory.R
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.presentation.components.FloatingLayout
import com.example.fitfactory.utils.BitmapHelper
import com.example.fitfactory.utils.Constants
import com.example.fitfactory.utils.PermissionManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.tasks.Task
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.map_fragment.*
import javax.inject.Inject

class MapFragment : BaseFragment(), OnMapReadyCallback {

    @Inject
    lateinit var activity: AppCompatActivity

    private var map: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: MapViewModel
    private lateinit var permissionManager: PermissionManager
    private var locationRequest: LocationRequest? = null
    private lateinit var locationCallback: LocationCallback
    private var isAbleToLocationUpdate: Boolean = false
    private var isCameraMoving: Boolean = false

    private lateinit var clusterManager: ClusterManager<MyClusterItem>

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.801556, 19.456455), 8f))
            setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.map_style
                )
            )
            uiSettings.isMapToolbarEnabled = false

            if (permissionManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                isMyLocationEnabled = true
            } else {
                permissionManager.sendRequest(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        setClusterManger()
        fetchFitnessClubs()
        setMapListeners()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.component.inject(this)
        viewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        permissionManager = PermissionManager(activity)
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
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity.applicationContext)
        checkLocationSettings()
        setListeners()
    }

    private fun setListeners() {
        mapFragment_floatingLayout.setFloatingLayoutListener(object : FloatingLayout.FloatingLayoutListener {
            override fun onClick() {
                mapFragment_floatingLayout.toggle()
            }
        })
    }


    override fun onPause() {
        super.onPause()
        if (isAbleToLocationUpdate) stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdates() {
        if (permissionManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    private fun checkLocationSettings() {
        createLocationRequest()
        locationRequest?.let {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(it)
            val client: SettingsClient = LocationServices.getSettingsClient(activity)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            task.apply {
                addOnSuccessListener {
                    isAbleToLocationUpdate = true
                    defineLocationCallback()
                    startLocationUpdates()
                }
                addOnFailureListener {
                    isAbleToLocationUpdate = false
                    if (exception is ResolvableApiException) {
                        try {
                            startIntentSenderForResult(
                                (exception as ResolvableApiException).resolution.intentSender,
                                Constants.REQUEST_CHECK_SETTINGS,
                                null,
                                0,
                                0,
                                0,
                                null
                            )
                        } catch (sendEx: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    }
                }
            }
        }

    }

    private fun defineLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    if (!isCameraMoving) moveCamera(LatLng(location.latitude, location.longitude))
                }
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()?.apply {
            interval = 60000
            fastestInterval = 30000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun moveCamera(latLng: LatLng) {
        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, map?.cameraPosition?.zoom ?: 13f
            )
        )
    }

    private fun fetchFitnessClubs() {
        clusterManager.addItem(
            MyClusterItem(
                LatLng(51.766259, 19.456518),
                BitmapHelper().bitmapDescriptorFromVector(R.drawable.marker),
                null
            )
        )
        clusterManager.addItem(
            MyClusterItem(
                LatLng(51.771258, 19.446819),
                BitmapHelper().bitmapDescriptorFromVector(R.drawable.marker_positive),
                null
            )
        )
        clusterManager.addItem(
            MyClusterItem(
                LatLng(51.776410, 19.460809),
                BitmapHelper().bitmapDescriptorFromVector(R.drawable.marker_negative),
                null
            )
        )
        clusterManager.addItem(
            MyClusterItem(
                LatLng(51.775062, 19.470068),
                BitmapHelper().bitmapDescriptorFromVector(R.drawable.marker),
                null
            )
        )
        clusterManager.renderer = ClusterIconRenderer(context, map, clusterManager)
    }

    private fun setClusterManger() {
        clusterManager = ClusterManager(context, map)
        clusterManager.apply {
            setAnimation(true)
        }
    }

    private fun setMapListeners() {
        map?.setOnCameraMoveStartedListener { isCameraMoving = true }
        map?.setOnCameraIdleListener { isCameraMoving = false }
        map?.setOnMarkerClickListener(clusterManager)
        clusterManager.setOnClusterItemClickListener {
            moveCamera(it.position)
            isCameraMoving = true
            mapFragment_floatingLayout.expand()
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.LOCATION_REQUEST_CODE) {
            if (permissionManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                map?.isMyLocationEnabled = true
            }
        } else {
            // Permission was denied. Display an error message.
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_CHECK_SETTINGS -> {
                    isAbleToLocationUpdate = true
                    defineLocationCallback()
                    startLocationUpdates()
                }
            }
        }
    }

}
