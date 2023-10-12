package it.step.moviecatalog.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import it.step.moviecatalog.MainActivity
import it.step.moviecatalog.R
import it.step.moviecatalog.databinding.FragmentCinemaMapBinding
import it.step.moviecatalog.model.Cinema
import it.step.moviecatalog.viewmodel.CinemaViewModel
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class CinemaMapFragment : Fragment() {

    private val cinemaViewModel: CinemaViewModel by viewModels()
    private lateinit var binding: FragmentCinemaMapBinding
    private lateinit var view: View

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback
    private var locationRequest: LocationRequest
    private var requestingLocationUpdates = false

    private lateinit var map: MapView
    private var startPoint = MutableLiveData(GeoPoint(40.85715546623741, 14.277825536894902))
    private lateinit var mapController: IMapController
    private var marker: Marker? = null

    private lateinit var mainActivity: Activity

    private var cinemaDataList: List<Cinema> = emptyList()

    init {
        locationRequest = LocationRequest.create()
            .apply {
                interval = 1000
                fastestInterval = 500
                smallestDisplacement = 10f //10m
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                maxWaitTime = 1000
            }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult
                for (location in locationResult.locations) {
                    updateLocation(location)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        initLoaction()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Configuration.getInstance().userAgentValue =
            "MovieCatalog"

        cinemaViewModel.getAllCinemas()

        (mainActivity as MainActivity).RequestLocPermissions()

        binding = FragmentCinemaMapBinding.inflate(layoutInflater)
        map = binding.cinemaMapView
        map.setBuiltInZoomControls(false)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        mapController = map.controller
        initMap()

        view = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFloatingButtons()

        cinemaViewModel.cinemaList.observe(viewLifecycleOwner) { newData ->
            if (newData != null) {
                cinemaDataList = newData
            }
            addCinemaMarkers()
        }

        startPoint.observe(viewLifecycleOwner) { newData ->
            if (newData != null) {
                mapController.setCenter(startPoint.value);
            }
        }

    }

    override fun onResume() {
        super.onResume()
        binding.cinemaMapView.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        if (requestingLocationUpdates) {
            requestingLocationUpdates = false
            stopLocationUpdates()
        }
        binding.cinemaMapView.onPause()
    }


    private fun addCinemaMarkers() {

        for (cinemaData in cinemaDataList) {
            val marker = Marker(map)
            marker.position = GeoPoint(cinemaData.latitude, cinemaData.longitude)
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location)
            marker.infoWindow = null
            map.overlays.add(marker)
        }

        map.invalidate()
    }

    fun updateLocation(newLocation: Location) {
        getPositionMarker().position = GeoPoint(newLocation.latitude, newLocation.longitude)
    }

    fun initLoaction() {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient((mainActivity as MainActivity))
        readLastKnownLocation()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    @SuppressLint("MissingPermission")
    fun readLastKnownLocation() {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    updateLocation(it)
                    startPoint.postValue(GeoPoint(it.latitude, it.longitude))
                }

            }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun initMap() {
        if (!requestingLocationUpdates) {
            requestingLocationUpdates = true
            startLocationUpdates()
        }
        mapController.setZoom(13.5)
        mapController.setCenter(startPoint.value);
        map.invalidate()
    }

    private fun getPositionMarker(): Marker {
        if (marker == null) {
            marker = Marker(map)
            marker!!.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker!!.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_location);
            map.overlays.add(marker)
        }
        return marker!!
    }

    private fun initFloatingButtons(){
        binding.gpsFloatingButton.setOnClickListener {
            readLastKnownLocation()
        }

        binding.plusFloatingButton.setOnClickListener {
            mapController.zoomIn()
        }

        binding.minusFloatingButton.setOnClickListener {
            mapController.zoomOut()
        }

    }

}