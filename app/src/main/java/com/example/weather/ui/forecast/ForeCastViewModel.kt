package com.example.weather.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.api.Resource
import com.example.weather.api.RetrofitBuilder
import com.example.weather.model.DataList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForeCastViewModel : ViewModel() {

    private val weatherLiveData = MutableLiveData<Resource<List<DataList>>>()

    fun getForeCast(latitude:Double, longitude:Double){
        viewModelScope.launch(Dispatchers.IO) {
            val retrofit = RetrofitBuilder.getInstance()
            weatherLiveData.postValue(Resource.loading(null))
            try {
                val forecastData = retrofit.getForeCast(
                    latitude,longitude,
                    "65d00499677e59496ca2f318eb68c049"
                )
                weatherLiveData.postValue(Resource.success(forecastData.list))
            }catch (e:Exception){
                weatherLiveData.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getWeatherData(): LiveData<Resource<List<DataList>>> {
        return weatherLiveData
    }
}