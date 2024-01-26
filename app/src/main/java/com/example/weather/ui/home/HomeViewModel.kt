package com.example.weather.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.api.Resource
import com.example.weather.api.RetrofitBuilder
import com.example.weather.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val weatherLiveData = MutableLiveData<Resource<WeatherData>>()

    fun getWeatherData(latitude:Double, longitude:Double){
        viewModelScope.launch(Dispatchers.IO) {
            val retrofit = RetrofitBuilder.getInstance()
            weatherLiveData.postValue(Resource.loading(null))
            try {
                val weatherData = retrofit.getCurrentWeather(
                    latitude,longitude,
                    "65d00499677e59496ca2f318eb68c049"
                )
                weatherLiveData.postValue(Resource.success(weatherData))
            }catch (e:Exception){
                weatherLiveData.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getWeatherData(): LiveData<Resource<WeatherData>> {
        return weatherLiveData
    }
}