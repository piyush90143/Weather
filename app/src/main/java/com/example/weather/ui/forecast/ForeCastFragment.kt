package com.example.weather.ui.forecast

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.api.Status
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.model.DataList
import com.example.weather.utils.navigateToSettings
import com.example.weather.utils.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class ForeCastFragment : Fragment() {

    private var _binding: FragmentForecastBinding? = null
    private lateinit var adapter : ForeCastListAdapter
    private var weatherList = ArrayList<DataList>()
    private lateinit var mViewModel: ForeCastViewModel
    private val binding get() = _binding!!
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewModel =
            ViewModelProvider(this)[ForeCastViewModel::class.java]

        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        setupObservers()
        setupRecyclerView()
        setRefreshView()
        return binding.root
    }

    private fun setRefreshView(){
        binding.container.setOnRefreshListener {
            binding.container.isRefreshing = false
            getLocation()
        }
    }

    private fun setupRecyclerView() {
        adapter =  ForeCastListAdapter(weatherList)
        binding.weatherList.layoutManager = LinearLayoutManager(requireContext())
        binding.weatherList.adapter = adapter
    }

    override fun onStart() {
        requestPermission.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        super.onStart()
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                navigateToSettings(requireContext())
            }
        }

    private fun setupObservers() {
        mViewModel.getWeatherData().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { dataList ->
                        weatherList.addAll(dataList)
                        adapter.notifyDataSetChanged()
                    }
                }
                Status.LOADING -> {
                }
                else -> {
                    showToast(requireContext(),"No Data Found")
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result
                val latitude =  (lastLocation)?.latitude ?: 0.0
                val longitude = (lastLocation)?.longitude ?: 0.0
                mViewModel.getForeCast(latitude,longitude)
            }
            else {
                showToast(requireContext(),"No location detected. Make sure location is enabled on the device.")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}