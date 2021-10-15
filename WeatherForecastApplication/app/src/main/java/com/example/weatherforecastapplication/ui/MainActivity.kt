package com.example.weatherforecastapplication.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.OpenWeatherApiInterface
import com.example.weatherforecastapplication.data.response.CurrentWeatherResponse
import com.example.weatherforecastapplication.data.response.Info
import com.example.weatherforecastapplication.data.response.WeatherForecastResponse
import com.example.weatherforecastapplication.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
const val API_KEY = "d8e2e4207b92a1c5659b0329d0ea4843"

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding
    lateinit var mCurrentWeatherResponse: CurrentWeatherResponse
    lateinit var mForecastResponse: WeatherForecastResponse
    var mInfoList: ArrayList<Info> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initAdapter()
        getCurrentWeather()
        getForecast()
    }

    private fun getCurrentWeather() {
        val retrofitBuilder= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(OpenWeatherApiInterface::class.java)
        val data = retrofitBuilder.getCurrentWeather(API_KEY, "Singapore")
        data.enqueue(object : Callback<CurrentWeatherResponse?> {
            override fun onResponse(call: Call<CurrentWeatherResponse?>, response: Response<CurrentWeatherResponse?>) {
                Log.i("Running", "onResponse")
                if(response.body() != null) {
                    mCurrentWeatherResponse = response.body()!!
                    mBinding.tvCity.text = mCurrentWeatherResponse.name

                    val celsius = String.format("%.2f", (mCurrentWeatherResponse.main.temp - 273.15)).toFloat()
                    mBinding.tvTemperature.text = celsius.toString() + " \u2103"
                }
            }

            override fun onFailure(call: Call<CurrentWeatherResponse?>, t: Throwable) {
                Log.i("Running", "onFailure "+t.message);
            }
        })
    }

    private fun getForecast() {
        val retrofitBuilder= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(OpenWeatherApiInterface::class.java)
        val data = retrofitBuilder.getWeatherForecast(API_KEY, "Singapore")
        data.enqueue(object : Callback<WeatherForecastResponse?> {
            override fun onResponse(call: Call<WeatherForecastResponse?>, response: Response<WeatherForecastResponse?>) {
                Log.i("Running", "onResponse")
                if(response.body() != null) {
                    mForecastResponse = response.body()!!
                    prepareList()
                }
            }

            override fun onFailure(call: Call<WeatherForecastResponse?>, t: Throwable) {
                Log.i("Running", "onFailure "+t.message);
            }
        })
    }

    //to filter the result based on Date
    fun prepareList() {
        var currentDate: String = ""
        for(i in mForecastResponse.list) {
            if(!convertDate(i.dt.toLong()).equals(currentDate)) {
                mInfoList.add(i)
            }
            currentDate = convertDate(i.dt.toLong())
        }
        bindUI()
    }

    fun convertDate(unixTimestamp: Long): String{
        var dateStr: String = ""
        val javaTimestamp = unixTimestamp * 1000L
        val date = Date(javaTimestamp)
        dateStr = SimpleDateFormat("dd/MM/yyyy").format(date)
        return dateStr
    }

    fun bindUI() {
        mBinding.tvCity.setText(mForecastResponse.city.name)
        initAdapter()
    }

    fun initAdapter() {
        mBinding.rvForecast.layoutManager = LinearLayoutManager(this)
        val myAdapter = ForecastAdapter(this, mInfoList)
        mBinding.rvForecast.adapter = myAdapter
    }

    class ForecastAdapter(var c: Context, var lists: ArrayList<Info>): RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

        var list: ArrayList<Info> = lists

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val textViewDay: TextView = v.findViewById(R.id.tvDay)
            val textViewTemperature: TextView = v.findViewById(R.id.tvTemperature)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_forecast, parent, false)
            return ViewHolder(inflatedView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val Info = list[position]

            val unixTimestamp: Long = Info.dt.toLong()
            val javaTimestamp = unixTimestamp * 1000L
            val date = Date(javaTimestamp)
            val sunrise: String = SimpleDateFormat("dd MMM yyyy").format(date)
            holder.textViewDay.text = sunrise

            val celsius = String.format("%.2f", (Info.main.temp - 273.15)).toFloat()
            holder.textViewTemperature.text = celsius.toString() + " \u2103"
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }
}