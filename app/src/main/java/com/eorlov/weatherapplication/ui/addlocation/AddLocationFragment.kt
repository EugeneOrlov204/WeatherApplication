package com.eorlov.weatherapplication.ui.addlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.eorlov.weatherapplication.R
import com.eorlov.weatherapplication.base.BaseFragment
import com.eorlov.weatherapplication.databinding.FragmentAddLocationBinding
import com.eorlov.weatherapplication.utils.Constants
import com.eorlov.weatherapplication.utils.Results
import com.eorlov.weatherapplication.utils.ext.hideKeyboard
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException


@AndroidEntryPoint
class AddLocationFragment : BaseFragment(), OnMapReadyCallback {
    private val viewModel: AddLocationViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: FragmentAddLocationBinding
    private lateinit var cityName: String
    private var mapView: MapView? = null
    private var map: GoogleMap? = null
    private var marker: Marker? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddLocationBinding.inflate(inflater, container, false)

        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadEvent.value = Results.OK
        setListeners()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
        printLog("On resume")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(0.0, 0.0)))
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    private fun setObservers() {
        viewModel.cityLocationLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                findCityOnMap(cityName, it)
            }
        }

        viewModel.loadEvent.apply {
            observe(viewLifecycleOwner) { event ->
                when (event) {
                    Results.OK -> {
                    }
                    Results.INTERNET_ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.internet_error),
                            Toast.LENGTH_LONG
                        ).show()
                        value = Results.OK
                    }
                    Results.EXISTED_CITY -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.exixted_city_error_message),
                            Toast.LENGTH_LONG
                        ).show()
                        value = Results.OK
                    }
                    Results.NOT_EXISTED_CITY -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.not_exixted_city_error_message),
                            Toast.LENGTH_LONG
                        ).show()
                        value = Results.OK
                    }
                    else -> {
                    }
                }
            }
        }

        viewModel.getWeatherLiveData.observe(viewLifecycleOwner) {

            lifecycleScope.launch {
                val weather = viewModel.getWeather(it)

                if(viewModel.isNotExistingWeather(weather)) {
                    viewModel.addItemToDatabase(weather)
                } else {
                    viewModel.loadEvent.value = Results.EXISTED_CITY
                }
                activity?.onBackPressed()
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            root.setOnClickListener {
                it.hideKeyboard()
            }

            buttonAddLocation.setOnClickListener {
                try {
                    if (marker != null) {
                        viewModel.initWeather(
                            marker?.position?.latitude.toString(),
                            marker?.position?.longitude.toString(),
                            Constants.APP_ID
                        )
                    }
                } catch (exception: IOException) {
                    viewModel.loadEvent.value = Results.INTERNET_ERROR
                }
            }

            textInputEditTextCityName.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    cityName = textInputEditTextCityName.text.toString()
                    if (cityName.isNotEmpty()) {
                        lifecycleScope.launch {
                            viewModel.initCityLocation(cityName)
                        }
                    }
                }
                false
            }
        }
    }

    private fun findCityOnMap(text: String, cityLocation: LatLng) {
        map?.moveCamera(
            CameraUpdateFactory.newLatLng(
                cityLocation
            )
        )

        addMarkerToMap(text, cityLocation)
    }

    private fun addMarkerToMap(text: String, latLng: LatLng) {
        if (marker != null) marker?.remove()
        marker = map?.addMarker(
            MarkerOptions()
                .position(latLng).title(text)
                .icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_CYAN
                    )
                )
                .draggable(false).visible(true)
        )
    }
}