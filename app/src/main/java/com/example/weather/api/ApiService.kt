package com.example.weather.api

import com.example.weather.model.ForeCastData
import com.example.weather.model.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/forecast?")
    suspend fun getForeCast(@Query("lat") lat:Double,@Query("lon") lon:Double, @Query("appid") appId:String): ForeCastData

    @GET("data/2.5/weather?")
    suspend fun getCurrentWeather(@Query("lat") lat:Double,@Query("lon") lon:Double, @Query("appid") appId:String): WeatherData
}