package com.eorlov.weatherapplication.ui.citiesweather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.eorlov.weatherapplication.R
import com.eorlov.weatherapplication.base.BaseFragment
import com.eorlov.weatherapplication.databinding.FragmentCitiesWeatherBinding
import com.eorlov.weatherapplication.model.Weather
import com.eorlov.weatherapplication.ui.citiesweather.adapter.CitiesWeatherRecyclerAdapter
import com.eorlov.weatherapplication.utils.Constants.HOMETOWN
import com.eorlov.weatherapplication.utils.Results
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CitiesWeatherFragment : BaseFragment() {
    private val citiesWeatherListAdapter: CitiesWeatherRecyclerAdapter
            by lazy(LazyThreadSafetyMode.NONE) {
                CitiesWeatherRecyclerAdapter()
            }
    private val viewModel: CitiesWeatherViewModel by viewModels()

    private lateinit var binding: FragmentCitiesWeatherBinding
    private lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCitiesWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initializeData()
        initSwipeRefreshLayout()
        initRecycler()
        setObservers()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateWeathersList() {
        lifecycleScope.launch {
            lockUI()
            viewModel.refreshItem(citiesWeatherListAdapter.currentList)
            viewModel.citiesWeatherListLiveData.value = viewModel.getAll().toMutableList()
            swipeContainer.isRefreshing = false
            unlockUI()
        }
    }


    private fun initSwipeRefreshLayout() {
        swipeContainer = binding.swipeRefresh

        swipeContainer.setOnRefreshListener {
            updateWeathersList()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun setListeners() {
        binding.buttonAddLocation.setOnClickListener {
            val action =
                CitiesWeatherFragmentDirections.actionCitiesWeatherFragmentToAddLocationFragment()
            findNavController().navigate(action)
        }

        binding.simpleDraweeViewHomePage.setOnClickListener {
            if (citiesWeatherListAdapter.currentList.find { it.locationName == HOMETOWN } != null) {
                val action =
                    CitiesWeatherFragmentDirections.actionCitiesWeatherFragmentToHomePageFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun setObservers() {
        viewModel.apply {
            citiesWeatherListLiveData.observe(viewLifecycleOwner) { list ->
                loadEvent.value = Results.OK
                citiesWeatherListAdapter.submitList(list.toMutableList())
            }

            loadEvent.apply {
                observe(viewLifecycleOwner) { event ->
                    when (event) {
                        Results.OK -> {
                            unlockUI()
                            binding.contentLoadingProgressBar.isVisible = false
                        }
                        Results.LOADING -> {
                            lockUI()
                            binding.contentLoadingProgressBar.isVisible = true
                        }
                        Results.INITIALIZE_DATA_ERROR -> {
                            unlockUI()
                            binding.contentLoadingProgressBar.isVisible = false
                            Toast.makeText(requireContext(), event.name, Toast.LENGTH_LONG).show()
                        }
                        Results.INTERNET_ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.internet_error),
                                Toast.LENGTH_LONG
                            ).show()
                            value = Results.OK
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }

    private fun initRecycler() {

        /* Variable that implements swipe-to-delete */
        val itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.END
            ) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    removeItemFromRecyclerView(
                        viewHolder.adapterPosition
                    )
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }
            }

        binding.recyclerViewCitiesWeather.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = citiesWeatherListAdapter

            //Implement swipe-to-delete
            ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(this)
        }
    }

    /**
     * Removes item on given position from RecyclerView
     */
    private fun removeItemFromRecyclerView(
        position: Int,
    ) {
        lifecycleScope.launch {
            val removedItem: Weather = viewModel.getItem(position) ?: return@launch
            viewModel.removeItem(position)
            Snackbar.make(
                binding.root,
                getString(R.string.removed_contact_message),
                LENGTH_LONG
            ).setAction("Cancel") {
                lifecycleScope.launch {
                    viewModel.addItem(position, removedItem)
                }
            }.show()
        }
    }
}


