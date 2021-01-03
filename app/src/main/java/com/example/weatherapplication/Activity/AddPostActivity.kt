package com.example.weatherapplication.Activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.weatherapplication.Interface.PostFirestoreModel
import com.example.weatherapplication.Model.CacheModel
import com.example.weatherapplication.Model.DiaryUtility
import com.example.weatherapplication.Model.PostFirestore
import com.example.weatherapplication.NetworkUtils
import com.example.weatherapplication.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import java.io.FileNotFoundException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddPostActivity : AppCompatActivity() {
    private lateinit var temperatureTextView: TextView
    private lateinit var postEditText: EditText
    private lateinit var tempText: String
    private lateinit var savePostButton: Button
    private lateinit var realm: Realm
    private lateinit var firestoreHelper: PostFirestoreModel
    private val cacheHelper: CacheModel = CacheModel()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        tempText = intent.getStringExtra("tempString").toString()
        temperatureTextView = findViewById(R.id.tempTextView)

        postEditText = findViewById(R.id.post_EditText)
        savePostButton = findViewById(R.id.uploadPost)
        handleNetworkChanges()

        setupUI()

    }


    private fun handleNetworkChanges()
    {

        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, { isConncted ->
            if (!isConncted)
            {
                savePostButton.setOnClickListener {
                    Toast.makeText(this, "Network is missing", Toast.LENGTH_SHORT).show()
                }
            }else{
                savePostButton.setOnClickListener {
                    saveDataToFirestore()

                }
            }
        })
    }


    private fun saveDataToFirestore (){
        if(DiaryUtility.validateDiaryInput(postEditText.text.toString())) {
            try {

                firestoreHelper = PostFirestoreModel()

                val db = Firebase.firestore
                val task = PostFirestore()

                val sdf = SimpleDateFormat("yyyy/M/dd hh:mm:ss")
                val currentDate = sdf.format(Date())

                task.temp = tempText
                task.diaryInput = postEditText.text.toString()
                task.creationDate = currentDate

                firestoreHelper.addToFirestore(task)

                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                //startActivity(Intent(this, MainActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "You can't post en empty post!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        val temp = tempText
        val concatenatedTempText = "Stockholm $temp ℃"
        temperatureTextView.text = concatenatedTempText
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}