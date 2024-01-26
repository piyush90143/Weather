package com.example.weather.ui.home

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.api.Status
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.utils.convertKelvinToFarenhit
import com.example.weather.utils.degToCompass
import com.example.weather.utils.navigateToSettings
import com.example.weather.utils.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var mViewModel: HomeViewModel
    private val binding get() = _binding!!
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupObservers()
        return binding.root
    }

    override fun onStart() {
        requestPermission.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        super.onStart()
    }

    private fun setupObservers() {
        mViewModel.getWeatherData().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        binding.cityName.text = data.name
                        binding.direction.text = degToCompass(data.wind.deg)
                        binding.windSpeed.text = "${data.wind.speed.toInt()} mph"

                        binding.feelslike.text = "Feels like: ${data.main.feels_like.convertKelvinToFarenhit()}"
                        binding.minmax.text = "${data.main.temp_max.convertKelvinToFarenhit()}/${data.main.temp_min.convertKelvinToFarenhit()}"
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

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                navigateToSettings(requireContext())
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    fun getLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result
                val latitude =  (lastLocation)?.latitude ?: 0.0
                val longitude = (lastLocation)?.longitude ?: 0.0
                mViewModel.getWeatherData(latitude,longitude)
            }
            else {
                showToast(requireContext(),"No location detected. Make sure location is enabled on the device.")
            }
        }
    }
}