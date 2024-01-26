package com.example.weather.model


data class ForeCastData(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<DataList>,
    val message: Int
)