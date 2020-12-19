package com.example.weatherapplication.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.weatherapplication.Adapter.PostFirestoreAdapter
import com.example.weatherapplication.Model.PostFirestore
import com.example.weatherapplication.Model.Variables
import com.example.weatherapplication.Model.WeatherDataModel
import com.example.weatherapplication.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.type.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.*
import java.util.*
import kotlin.math.roundToInt
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private lateinit var addPost: FloatingActionButton
    private lateinit var mFirestoreAdapter: PostFirestoreAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var PERMISSION_ID = 1000
    var userLat: Double = 0.0
    var userLng: Double = 0.0

    private var temperatureTextView: TextView? = null
    private val client = OkHttpClient()
    private val API_URL =
        "https://api.openweathermap.org/data/2.5/weather?q=stockholm&units=metric&appid=8df6e9cbc37e2471dea928884f364bf3"

    private var tempString: String? = null

    private val apiCallBack: WeatherDataModel = WeatherDataModel()
    var TAG: String? = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addPost = findViewById(R.id.addPost)
        val textView: TextView = findViewById(R.id.temperature)
        temperatureTextView = textView;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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

    private fun checkPermission(): Boolean {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        if(requestCode == PERMISSION_ID) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug:", "You have permission to use LocationManager")
            }
        }
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if(location != null) {
                        userLat = location.latitude
                        userLng = location.longitude
                        println("!!! Lat: $userLat, Lng: $userLng")
                    } else {
                        Toast.makeText(this, "No location found.", Toast.LENGTH_SHORT)
                    }
                }
    }

    private fun checkIfNetwork() {
        if (Variables.isNetworkConnected) {
            //Do something when network is connected

            getFirestoreToRV()
            getWeatherAsync()
            getLastKnownLocation()

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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
