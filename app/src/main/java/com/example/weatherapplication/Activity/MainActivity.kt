package com.example.weatherapplication.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.weatherapplication.Interface.PostFirestoreModel
import com.example.weatherapplication.RecyclerViewAdapter.PostFirestoreAdapter
import com.example.weatherapplication.Model.CacheModel
import com.example.weatherapplication.Model.PostFirestore
import com.example.weatherapplication.Model.SortingFunctions
import com.example.weatherapplication.Model.WeatherDataModel
import com.example.weatherapplication.NetworkUtils
import com.example.weatherapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.Exception
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

        addPost.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            intent.putExtra("tempString", tempString)
            startActivity(intent)
        }

    }


    private fun handleNetworkChanges()
    {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, { isConnected ->

            // If there is no network available, try to populate the recyclerview based on the cache file.
            if (!isConnected)
            {
                println("!!! handleNetwork NOT connected called")
                modeTextview?.text = "Offline Mode"

                try {

                    val diaryInputsList = cacheHelper.readCachedFile(applicationContext)
                    for (diary in diaryInputsList) {
                        val p = diary.diaryInput
                        println("!!! Main, Cache before populate (offline): $p")
                    }
                    populateTheRecyclerView(diaryInputsList, false)

                }catch (e: FileNotFoundException){
                    Log.d(TAG, "Cache file not found", e)
                }
            }
            else
            {
                // If there is a network available, get firestore data and populate the RV based on that.
                modeTextview?.text = "Online Mode"
                getFirestoreToRV()
                getWeatherAsync()
            }
        })
    }

    private fun getFirestoreToRV() {
        println("!!! getFirestoreToRV called")
        val firestoreModel = PostFirestoreModel()
        firestoreModel.getFromFirestore(applicationContext) { list ->
            populateTheRecyclerView(list, true)
        }
    }

    private fun populateTheRecyclerView(notesList: MutableList<PostFirestore>, isConnected: Boolean) {
        val sortedList = SortingFunctions.dateInsertionSorting(notesList)
        mFirestoreAdapter = PostFirestoreAdapter(sortedList)
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
        // put your code here...'
        handleNetworkChanges()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}