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
import com.example.weatherapplication.NetworkUtils
import com.example.weatherapplication.R
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

        setupUI()
        handleNetworkChanges()



    }


    private fun handleNetworkChanges()

    {

        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, { isConnected ->
            if (!isConnected)
            {
                savePostButton.setOnClickListener {
                    saveDataToCache()

                }

            }
            else
            {

                savePostButton.setOnClickListener {
                    saveDataToFirestore()

                }

            }
        })
    }

    private fun saveDataToCache(){
        //In case the application is working in offline mode, we should save new posts to cache instead of to firestore.
        firestoreHelper = PostFirestoreModel()

        val task = PostFirestore()
        val currentDate = getCurrentDate()
        val diaryInputsList = cacheHelper.readCachedFile(this)

        task.temp = tempText
        task.diaryInput = postEditText.text.toString()
        task.creationDate = currentDate

        diaryInputsList.add(task)
        firestoreHelper.addToFirestore(task)
        cacheHelper.createCachedFile(this, diaryInputsList)
        finish()

    }

    private fun saveDataToFirestore (){
        if(DiaryUtility.validateDiaryInput(postEditText.text.toString())) {
            try {

                firestoreHelper = PostFirestoreModel()
                val task = PostFirestore()
                val currentDate = getCurrentDate()

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

    private fun getCurrentDate() : String{
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
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