package it.step.moviecatalog.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.step.moviecatalog.MainActivity
import it.step.moviecatalog.R
import it.step.moviecatalog.databinding.FragmentCinemaMapBinding
import it.step.moviecatalog.model.Cinema
import it.step.moviecatalog.viewmodel.CinemaViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class CinemaMapFragment : Fragment() {

    private val cinemaViewModel: CinemaViewModel by viewModels()
    private lateinit var bindingCinemaMap: FragmentCinemaMapBinding
    private lateinit var view: View

    private var cinemaDataList: List<Cinema> = emptyList()

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        cinemaViewModel.getAllCinemas()

        bindingCinemaMap = FragmentCinemaMapBinding.inflate(layoutInflater)
        view = bindingCinemaMap.root
        mapView = view.findViewById(R.id.cinema_mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK) // Imposta la sorgente delle tile (puoi personalizzarla)
        mapView.setMultiTouchControls(true)
        mapView.setBuiltInZoomControls(true)
        mapView.controller.setZoom(15.0) // Imposta il livello di zoom iniziale
        mapView.controller.setCenter(GeoPoint(40.85717564223902, 14.277806824328868)) // Imposta il centro della mappa alle coordinate desiderate (latitudine e longitudine)

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cinemaViewModel.cinemaList.observe(viewLifecycleOwner) { newData ->
            if (newData != null) {
                cinemaDataList = newData
            }
            addCinemaMarkers()
        }

    }

    private fun addCinemaMarkers() {

            for (cinemaData in cinemaDataList) {
                val marker = Marker(mapView)
                marker.position = GeoPoint(cinemaData.latitude, cinemaData.longitude)
                marker.title = cinemaData.name
                marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location)

                mapView.overlays.add(marker)
            }

        mapView.invalidate()
    }


}