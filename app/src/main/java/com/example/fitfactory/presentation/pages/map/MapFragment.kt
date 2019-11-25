package com.example.fitfactory.presentation.pages.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fitfactory.R
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.presentation.customViews.FloatingLayout
import com.example.fitfactory.utils.BitmapHelper
import com.example.fitfactory.constants.RequestCode
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
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject

class MapFragment : BaseFragment(), OnMapReadyCallback {

    @Inject
    lateinit var activity: AppCompatActivity

    private var map: GoogleMap? = null
    private var lastKnownLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel by lazy { MapViewModel() }
    private lateinit var permissionManager: PermissionManager
    private var locationRequest: LocationRequest? = null
    private lateinit var locationCallback: LocationCallback
    private var isCameraMoving: Boolean = false
    private var requestingLocationUpdates: Boolean = false
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
        updatesValuesFromBundle(savedInstanceState)
        Injector.component.inject(this)
        permissionManager = PermissionManager(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment_map) as SupportMapFragment

        mapFragment.getMapAsync(this)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity.applicationContext)
        checkLocationSettings()
        setListeners()
    }

    override fun flexibleViewEnabled() = true
    override fun paddingTopEnabled() = false
    override fun topBarTitle() = getString(R.string.app_name)
    override fun topBarEnabled() = true
    override fun backButtonEnabled(): Boolean = false

    private fun setListeners() {
        mapFragment_floatingLayout.setFloatingLayoutListener(object :
            FloatingLayout.FloatingLayoutListener {
            override fun onClick() {
                mapFragment_floatingLayout.toggle()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        if (requestingLocationUpdates) stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdates() {
        if (permissionManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
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
                    requestingLocationUpdates = true
                    defineLocationCallback()
                    startLocationUpdates()
                }
                addOnFailureListener {
                    requestingLocationUpdates = false
                    if (exception is ResolvableApiException) {
                        try {
                            startIntentSenderForResult(
                                (exception as ResolvableApiException).resolution.intentSender,
                                RequestCode.REQUEST_CHECK_SETTINGS,
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
                    lastKnownLocation = location
                    if (!isCameraMoving) moveCamera(
                        LatLng(location.latitude, location.longitude),
                        map?.cameraPosition?.zoom ?: 13f
                    )
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

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, zoom
            )
        )
    }

    private fun fetchFitnessClubs() {

        viewModel.fetchFitnessClubs()
        viewModel.fitnessClubRepository.getAll().observe(viewLifecycleOwner, Observer {
            clusterManager.clearItems()
            map?.clear()
            it.forEach {
                clusterManager.addItem(
                    MyClusterItem(it, BitmapHelper().bitmapDescriptorFromVector(R.drawable.marker), it.id)
                )
            }
            clusterManager.cluster()
        })
    }

    private fun setClusterManger() {
        clusterManager = ClusterManager(context, map)
        clusterManager.apply {
            setAnimation(true)
        }
    }

    private fun setMapListeners() {
        clusterManager.renderer = ClusterIconRenderer(context, map, clusterManager)
        clusterManager.setOnClusterClickListener {
            moveCamera(it.position, 11f)
            isCameraMoving = true
            true
        }

        clusterManager.setOnClusterItemClickListener {

            moveCamera(it.position, 15f)
            isCameraMoving = true
            if (!mapFragment_floatingLayout.isAnimated) {
                mapFragment_floatingLayout.fitnessClub = it.fitnessClub
                lastKnownLocation?.let { l ->
                    val distance = l.distanceTo(Location("club").apply {
                        latitude = it.position.latitude
                        longitude = it.position.longitude
                    })
                    mapFragment_floatingLayout.setDistance(distance / 1000)
                }
                mapFragment_floatingLayout.expand()
            }
            true
        }

        map?.setOnCameraMoveStartedListener { isCameraMoving = true }
        map?.setOnCameraIdleListener(clusterManager)
        map?.setOnMarkerClickListener(clusterManager)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RequestCode.LOCATION_REQUEST_CODE) {
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
                RequestCode.REQUEST_CHECK_SETTINGS -> {
                    requestingLocationUpdates = true
                    defineLocationCallback()
                    startLocationUpdates()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(RequestCode.REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates)
        super.onSaveInstanceState(outState)
    }

    private fun updatesValuesFromBundle(savedInstanceState: Bundle?) {
        savedInstanceState ?: return
        if (savedInstanceState.keySet().contains(RequestCode.REQUESTING_LOCATION_UPDATES_KEY)) {
            requestingLocationUpdates =
                savedInstanceState.getBoolean(RequestCode.REQUESTING_LOCATION_UPDATES_KEY)
        }
    }

}
