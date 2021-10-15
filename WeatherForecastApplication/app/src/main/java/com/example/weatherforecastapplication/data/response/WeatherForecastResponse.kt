package com.example.weatherforecastapplication.data.response


data class WeatherForecastResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Info>,
    val message: Int
)