package com.example.weatherapplication.Activity

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
import com.example.weatherapplication.R
import java.io.FileNotFoundException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddPostActivity : AppCompatActivity() {
    private lateinit var temperatureTextView: TextView
    private lateinit var postEditText: EditText
    private lateinit var tempText: String
    private lateinit var savePostButton: Button
    private lateinit var firestoreHelper: PostFirestoreModel
    private val cacheHelper: CacheModel = CacheModel()
    private val TAG: String = "AddPostActivity"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        tempText = intent.getStringExtra("tempString").toString()
        temperatureTextView = findViewById(R.id.tempTextView)

        postEditText = findViewById(R.id.post_EditText)
        savePostButton = findViewById(R.id.uploadPost)

        setupUI()
        //checkForCacheFile()

        savePostButton.setOnClickListener {
            saveData()
        }

    }


    private fun saveData(){

        if(DiaryUtility.validateDiaryInput(postEditText.text.toString())) {
            try {

                //In case the application is working in offline mode, we should save new posts to cache instead of to firestore.
                firestoreHelper = PostFirestoreModel()

                val task = PostFirestore()
                val currentDate = getCurrentDate()


                // If cache file exists, add the new diary input to the cache file, and then try and save data to firestore.
                if (checkForCacheFile()) {

                    val diaryInput = PostFirestore()

                    diaryInput.temp = tempText
                    diaryInput.diaryInput = postEditText.text.toString()
                    diaryInput.creationDate = currentDate

                    cacheHelper.addToCacheFile(applicationContext, diaryInput)

                    // Try and save data to Firestore. If the network is unavailable, this data will be sent to Firestore later due to built-in offline data-persistence.
                    firestoreHelper.addToFirestore(diaryInput)
                    finish()

                } else {

                    // If cache file does not exist, create a cache file and add the diary input to it.
                    val diaryInputsList = mutableListOf<PostFirestore>()

                    task.temp = tempText
                    task.diaryInput = postEditText.text.toString()
                    task.creationDate = currentDate
                    diaryInputsList.add(task)

                    cacheHelper.createCachedFile(applicationContext, diaryInputsList)
                    firestoreHelper.addToFirestore(task)

                    finish()

                }
            } catch (e: Exception) {
                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                println("!!! Exception addPost: $e")
            }
        }
    }


    private fun checkForCacheFile(): Boolean {
        return try {
            cacheHelper.readCachedFile(applicationContext)
            true

        }catch (e: FileNotFoundException){
            Log.d(TAG, "Cache file not found", e)
            false
        }
    }

    private fun getCurrentDate() : String{
        val sdf = SimpleDateFormat("yyyy/M/dd hh:mm:ss")
        return sdf.format(Date())
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