package com.example.weatherapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmResults
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.properties.Delegates

import com.example.weatherapplication.Activity.AddPostActivity
import com.example.weatherapplication.Activity.SelectedActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.example.weatherapplication.Model.Post

class MainActivity : AppCompatActivity() {

    private lateinit var addPost: FloatingActionButton
    private lateinit var postList: ArrayList<Post>
    private lateinit var realm: Realm
    private lateinit var mAdapter: PostAdapter
    private lateinit var mUploads: List<Post>



    private var temperatureTextView: TextView? = null
    private val client = OkHttpClient()
    private val API_URL =
        "https://api.openweathermap.org/data/2.5/weather?q=stockholm&units=metric&appid=8df6e9cbc37e2471dea928884f364bf3"

    private var tempString: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()
        addPost = findViewById(R.id.addPost)

        mUploads = mutableListOf()


        val textView: TextView = findViewById(R.id.temperature)
        temperatureTextView = textView;
        getWeatherAsync()
        getAllPosts()

        addPost.setOnClickListener {
            val intent = Intent(this@MainActivity, AddPostActivity::class.java)
            intent.putExtra("tempString", tempString)
            startActivity(intent)

        }


        //TODO: Change so it is a longClickListener on every object separately
        addPost.setOnLongClickListener {

            realm.beginTransaction()
            realm.deleteAll()
            realm.commitTransaction()

            getAllPosts()

            return@setOnLongClickListener true
        }
    }


    private fun getAllPosts() {
        postList = ArrayList()

        val results: RealmResults<Post> = realm.where(
            Post::class.java
        ).findAll()


        mAdapter = PostAdapter(this, results)
        postRV.adapter = mAdapter
        postRV.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)

        mAdapter.onItemClick = { realmVariables ->

            val intent = Intent(applicationContext, SelectedActivity::class.java)
            intent.putExtra("realmId", realmVariables.id)
            intent.putExtra("realmText", realmVariables.text)
            intent.putExtra("realmTemp", realmVariables.temp)
            startActivity(intent)

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
