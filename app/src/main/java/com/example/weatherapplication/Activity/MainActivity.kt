package com.example.weatherapplication.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.weatherapplication.Adapter.PostFirestoreAdapter
import com.example.weatherapplication.Model.PostFirestore
import com.example.weatherapplication.Model.Variables
import com.example.weatherapplication.R
import com.example.weatherapplication.WeatherData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.*
import java.util.*
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private lateinit var addPost: FloatingActionButton
    private lateinit var mFirestoreAdapter: PostFirestoreAdapter
    private var temperatureTextView: TextView? = null
    private val client = OkHttpClient()
    private val API_URL =
        "https://api.openweathermap.org/data/2.5/weather?q=stockholm&units=metric&appid=8df6e9cbc37e2471dea928884f364bf3"

    private var tempString: String? = null
    var TAG: String? = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addPost = findViewById(R.id.addPost)
        val textView: TextView = findViewById(R.id.temperature)
        temperatureTextView = textView;
        checkIfNetwork()


        addPost.setOnClickListener {
            val isConnected: Boolean = Variables.isNetworkConnected

            if (isConnected) {
                val intent = Intent(this@MainActivity, AddPostActivity::class.java)
                intent.putExtra("tempString", tempString)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfNetwork() {
        if (Variables.isNetworkConnected) {
            //Do something when network is connected

            getFirestoreToRV()
            getWeatherAsync()

        } else if (!Variables.isNetworkConnected) {
            //Do something when network is not connected

            addPost.setOnClickListener {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            }

            val `in` = ObjectInputStream(
                FileInputStream(
                    File(
                        File(
                            cacheDir,
                            ""
                        ).toString() + "cacheFile.tmp"
                    )
                )
            )
            val mutableListObject = `in`.readObject()
            Log.d("TAG", "CACHE$mutableListObject")
            `in`.close()
        }
    }


    private fun getFirestoreToRV() {

        try {
            val settings = firestoreSettings {
                isPersistenceEnabled = false
            }

            // Access a Cloud Firestore instance
            val db = Firebase.firestore
            db.firestoreSettings = settings


            val notesList = mutableListOf<PostFirestore>()

            db.collection("DiaryInputs")
                .addSnapshotListener { snapshot, e ->
                    notesList.clear()

                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        for (doc in snapshot.documents!!) {
                            val note = doc.toObject(PostFirestore::class.java)
                            notesList.add(note!!)

                            val out: ObjectOutput = ObjectOutputStream(
                                FileOutputStream(
                                    File(
                                        cacheDir, ""
                                    ).toString() + "cacheFile.tmp"
                                )
                            )
                            out.writeObject(notesList)
                            Log.d("TAG", "notesList in cache:  $notesList")
                            out.close()
                        }
                        mFirestoreAdapter = PostFirestoreAdapter(notesList)
                        postRV.adapter = mFirestoreAdapter
                        postRV.layoutManager = StaggeredGridLayoutManager(
                            1,
                            LinearLayoutManager.VERTICAL
                        )

                        mFirestoreAdapter.onItemClick = { firestoreVariables ->

                            val intent = Intent(
                                applicationContext,
                                SelectedActivity::class.java
                            )
                            intent.putExtra("id", firestoreVariables.id)
                            intent.putExtra("diaryInput", firestoreVariables.diaryInput)
                            intent.putExtra("temp", firestoreVariables.temp)
                            startActivity(intent)
                        }


                        Log.d(TAG, "Current data: ${snapshot.documents}")
                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }
        }catch (e: Exception){
            Log.d(TAG, "Failure", e)
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
            var name: String by Delegates.observable("<>") { prop, old, new ->
                Log.d("DEBUG", "OBSERVER: $old -> $new")
            }
            tempString = weatherData.main.temp.toString()
            name = tempString as String
            temperatureTextView?.text = "Temp in Sthlm is: $name \u2103"
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
