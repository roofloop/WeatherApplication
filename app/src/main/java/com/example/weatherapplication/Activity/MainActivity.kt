package com.example.weatherapplication.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.properties.Delegates

import com.example.weatherapplication.Adapter.PostFirestoreAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.example.weatherapplication.Model.PostFirestore
import com.example.weatherapplication.R
import com.example.weatherapplication.WeatherData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var addPost: FloatingActionButton
    private lateinit var mFirestoreAdapter: PostFirestoreAdapter


    private var temperatureTextView: TextView? = null
    private val client = OkHttpClient()
    private val API_URL =
        "https://api.openweathermap.org/data/2.5/weather?q=stockholm&units=metric&appid=8df6e9cbc37e2471dea928884f364bf3"

    private var tempString: String? = null
    var TAG:String? = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addPost = findViewById(R.id.addPost)

        val textView: TextView = findViewById(R.id.temperature)
        temperatureTextView = textView;
        getWeatherAsync()
        getFirestoreToRV()

        addPost.setOnClickListener {
            val intent = Intent(this@MainActivity, AddPostActivity::class.java)
            intent.putExtra("tempString", tempString)
            startActivity(intent)
        }
    }

    private fun getFirestoreToRV() {
        // Access a Cloud Firestore instance
        val db = Firebase.firestore
        db.collection("DiaryInputs")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val notesList = mutableListOf<PostFirestore>()
                    for (doc in task.result!!) {
                        val note = doc.toObject(PostFirestore::class.java)
                        notesList.add(note)
                    }

                    mFirestoreAdapter = PostFirestoreAdapter(notesList)
                    postRV.adapter = mFirestoreAdapter
                    postRV.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)

                    mFirestoreAdapter.onItemClick = { firestoreVariables ->

                        val intent = Intent(applicationContext, SelectedActivity::class.java)
                        intent.putExtra("id", firestoreVariables.id)
                        intent.putExtra("diaryInput", firestoreVariables.diaryInput)
                        intent.putExtra("temp", firestoreVariables.temp)
                        startActivity(intent)

                    }

                } else {
                    Log.d("TAG", "Error getting documents: ", task.exception)
                }
            }
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
            temperatureTextView?.text = "Temp in Sthlm is: $name \u2103"
        }
    }
}
