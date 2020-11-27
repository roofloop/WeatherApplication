package com.example.weatherapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {
    private var temperatureTextView: TextView? = null

    private val client = OkHttpClient()
    private val API_URL =
        "https://api.openweathermap.org/data/2.5/weather?q=stockholm&units=metric&appid=8df6e9cbc37e2471dea928884f364bf3"

    private var tempString: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val getCurrentWeatherButton: Button = findViewById(R.id.getWeatherButton)
        getCurrentWeatherButton?.setOnClickListener()
        {
            getWeatherAsync()
        }

        val textView: TextView = findViewById<TextView>(R.id.temperature)
        temperatureTextView = textView;
    }

    private fun getWeatherAsync() {

        val request = Request.Builder()
            .url(API_URL)
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

                var jsonObject: JSONObject? = JSONObject(responseString)
                val gson = Gson()
                val weatherData: WeatherData = gson.fromJson(
                    jsonObject.toString(),
                    WeatherData::class.java
                )

                Thread(Runnable {
                    runOnUiThread {
                        updateUI(weatherData)
                    }
                }).start()
            }
        })
    }

    private fun updateUI(weatherData: WeatherData?) {
        if (weatherData != null) {

            var name: String by Delegates.observable("<>") {
                    prop, old, new ->
                Log.d("DEBUG","OBSERVER: $old -> $new")
            }

            tempString = weatherData.main.temp.toString()
            name = tempString as String
            temperatureTextView?.text = "Temp in Sthlm is: $name Celsius"

        }
    }
}
