package com.example.weatherapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getCurrentWeatherButton : Button = findViewById(R.id.getWeatherButton)
        val textView : TextView = findViewById<TextView>(R.id.temperature)

        run("https://api.openweathermap.org/data/2.5/weather?q=stockholm&units=metric&appid=8df6e9cbc37e2471dea928884f364bf3")
    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                Log.d("ERROR", "Failed to execute request: " + e.printStackTrace())
                e.printStackTrace();
            }

            override fun onResponse(call: Call, response: Response) {

                if (!response.isSuccessful) {
                    throw IOException("Unexpected code " + response);
                }
                try {
                    val responseData = response.body()!!.string()
                    val json = JSONObject(responseData)
                    val name = json.getString("name")

                    Log.d("Name: ", "Name from json object: " + name.toString())

                } catch (e: JSONException) {

                }
            }
        })
    }
}