package com.example.weatherforecastapplication.data.response


import com.google.gson.annotations.SerializedName

data class Info(
    val clouds: Clouds,
    val dt: Int,//date
    @SerializedName("dt_txt")
    val dtTxt: String,
    val main: Main,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)