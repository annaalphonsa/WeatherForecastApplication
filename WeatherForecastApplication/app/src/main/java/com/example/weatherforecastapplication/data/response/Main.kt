package com.example.weatherforecastapplication.data.response


import com.google.gson.annotations.SerializedName

data class Main(
    val feelsLike: Double,
    val grndLevel: Int,
    val humidity: Int,
    val pressure: Int,
    val seaLevel: Int,
    val temp: Double,
    val tempKf: Double,
    val tempMax: Double,
    val tempMin: Double
)