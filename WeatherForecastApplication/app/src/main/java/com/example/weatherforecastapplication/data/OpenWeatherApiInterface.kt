package com.example.weatherforecastapplication.data

import com.example.weatherforecastapplication.data.response.CurrentWeatherResponse
import com.example.weatherforecastapplication.data.response.WeatherForecastResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiInterface {

    @GET("weather")
    fun getCurrentWeather(@Query("appid") apiKey: String, @Query("q") location: String): Call<CurrentWeatherResponse>

    @GET("forecast")
    fun getWeatherForecast(@Query("appid") apiKey: String, @Query("q") location: String): Call<WeatherForecastResponse>
}