package com.example.weather.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    companion object {
        private var instance : ApiService? = null

        @Synchronized
        fun getInstance(): ApiService {
            if (instance == null)
                instance = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(" https://api.openweathermap.org/")
                    .build()
                    .create(ApiService::class.java)
            return instance as ApiService
        }
    }

}