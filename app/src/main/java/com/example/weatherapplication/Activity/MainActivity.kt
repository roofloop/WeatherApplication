package com.example.weatherapplication.Activity

import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.weatherapplication.Adapter.PostFirestoreAdapter
import com.example.weatherapplication.Model.CacheModel
import com.example.weatherapplication.Model.PostFirestore
import com.example.weatherapplication.Model.WeatherDataModel
import com.example.weatherapplication.NetworkUtils
import com.example.weatherapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.*
import java.util.*
import kotlin.math.roundToInt
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private lateinit var addPost: FloatingActionButton
    private lateinit var mFirestoreAdapter: PostFirestoreAdapter
    private var temperatureTextView: TextView? = null
    private var modeTextview: TextView? = null
    private val API_URL =
        "https://api.openweathermap.org/data/2.5/weather?q=stockholm&units=metric&appid=8cd3c8555e090df0d6917f60f2cc91a6"

    private var tempString: String? = null
    private val apiCallBack: WeatherDataModel = WeatherDataModel()
    private val cacheHelper: CacheModel = CacheModel()

    var TAG: String? = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addPost = findViewById(R.id.addPost)
        val textView: TextView = findViewById(R.id.temperature)
        val textViewOfflineMode: TextView = findViewById(R.id.offlineMode)
        temperatureTextView = textView
        modeTextview = textViewOfflineMode
        handleNetworkChanges()

    }


    private fun handleNetworkChanges()
    {

        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, { isConncted ->
            if (!isConncted)
            {
                modeTextview?.text = "Offline Mode"

                addPost.setOnClickListener {

                    Toast.makeText(this, "Network Fail", Toast.LENGTH_SHORT).show()

                }
                try {

                    populateTheRecyclerView(cacheHelper.readCachedFile(this), false)
                    val notesList = cacheHelper.readCachedFile(this)

                    for (item in notesList)
                        Log.d(TAG, "Items in noteslist from cache: ${item.creationDate}")

                }catch (e: FileNotFoundException){
                    Log.d(TAG, "Cache file not found", e)
                }
            }
            else
            {

                modeTextview?.text = "Online Mode"

                getFirestoreToRV()
                getWeatherAsync()

                addPost.setOnClickListener {
                        val intent = Intent(this, AddPostActivity::class.java)
                        intent.putExtra("tempString", tempString)
                        startActivity(intent)
                }
            }
        })
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

            db.collection("InputsDiary")
                .addSnapshotListener { snapshot, e ->
                    notesList.clear()

                    if (snapshot != null && !snapshot.isEmpty) {
                        for (doc in snapshot.documents) {
                            val note = doc.toObject(PostFirestore::class.java)
                            notesList.add(note!!)
                            cacheHelper.createCachedFile(this, notesList)
                        }

                        populateTheRecyclerView(notesList,true)


                    } else {
                        //Refreshing the RV and cache if firestore is empty.
                        cacheHelper.deleteCachedFile(this)
                        populateTheRecyclerView(notesList, true)
                    }
                }
        }catch (e: Exception){
            Log.d(TAG, "Failure", e)
        }
    }


    private fun populateTheRecyclerView(notesList: MutableList<PostFirestore>, isConnected: Boolean) {
        mFirestoreAdapter = PostFirestoreAdapter(notesList)
        postRV.adapter = mFirestoreAdapter
        postRV.layoutManager = StaggeredGridLayoutManager(
            1,
            LinearLayoutManager.VERTICAL
        )

        if (isConnected){
            mFirestoreAdapter.onItemClick = { firestoreVariables ->

                val intent = Intent(applicationContext, SelectedActivity::class.java)
                intent.putExtra("id", firestoreVariables.id)
                intent.putExtra("diaryInput", firestoreVariables.diaryInput)
                intent.putExtra("temp", firestoreVariables.temp)
                startActivity(intent)
            }
        }
    }


    private fun getWeatherAsync() {

        apiCallBack.fetchWeather(API_URL) { temp ->
            Thread(Runnable {
                runOnUiThread {
                    updateUI(temp)
                }
            }).start()
        }
    }

    private fun updateUI(weatherData: Double?) {

        if (weatherData != null) {
            var name: String by Delegates.observable("<>") { prop, old, new ->
                Log.d("DEBUG", "OBSERVER: $old -> $new")
            }
            tempString = weatherData.roundToInt().toString()
            name = tempString as String
            temperatureTextView?.text = "Temp in Sthlm is: $name \u2103"
        }
    }


    override fun onResume() {
        super.onResume()
        // put your code here...
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}