package com.example.weatherapplication.Model

import android.util.Log
import com.example.weatherapplication.WeatherData
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class WeatherDataModel {
    private val client = OkHttpClient()

    fun fetchWeather(apiUrl: String): Double? {
        var returnData: Double? = null

        val request = Request.Builder()
                .url(apiUrl)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                Log.d("ERROR", "Failed to execute request: " + e.printStackTrace())
                e.printStackTrace();
            }

            override fun onResponse(call: Call, response: Response) {

                val responseString = response.body()!!.string()

                /**
                 * Parse JSON response to Gson library
                 */
                val jsonObject: JSONObject? = JSONObject(responseString)
                val gson = Gson()
                val weatherData: WeatherData = gson.fromJson(
                        jsonObject.toString(),
                        WeatherData::class.java
                )
                returnData = weatherData.main.temp
            }
        })
        return returnData
    }
}