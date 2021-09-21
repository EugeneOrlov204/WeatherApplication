package com.eorlov.weatherapplication.ui.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eorlov.weatherapplication.base.BaseFragment
import com.eorlov.weatherapplication.databinding.FragmentHomePageBinding
import com.eorlov.weatherapplication.utils.Results
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomePageFragment : BaseFragment() {

    private val viewModel: HomePageViewModel by viewModels()

    private lateinit var binding: FragmentHomePageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setObservers()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }

    private fun initData() {
        lifecycleScope.launch {
            viewModel.initializeData()
            viewModel.homePageWeatherLiveData.value?.apply {
                binding.apply {
                    simpleDraweeViewWeatherType.setImageURI(weatherType)
                    textViewAddress.text = locationName

                    val coordinates = "$latitude, $longitude"
                    textViewCoordinates.text = coordinates

                    textViewData.text = date

                    val humidity = "Humidity: $humidity"
                    textViewHumidity.text = humidity

                    val windSpeed = "Wind speed: $windSpeed"
                    textViewWindSpeed.text = windSpeed

                    textViewTemperature.text = temperature
                    textViewTime.text = time
                }
            }
        }
    }

    private fun setListeners() {
        binding.simpleDraweeViewCitiesWeather.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setObservers() {
        viewModel.loadEvent.apply {
            observe(viewLifecycleOwner) { event ->
                when (event) {
                    Results.INITIALIZE_DATA_ERROR -> {
                        Toast.makeText(requireContext(), event.name, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                    }
                }
            }
        }
    }
}