package com.example.weatherforecastapplication.data.response


import com.google.gson.annotations.SerializedName

data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)