package com.example.weatherapplication.Activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.weatherapplication.Interface.PostFirestoreModel
import com.example.weatherapplication.Model.PostFirestore
import com.example.weatherapplication.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import java.lang.Exception

class AddPostActivity : AppCompatActivity() {
    private lateinit var temperatureTextView: TextView
    private lateinit var postEditText: EditText
    private lateinit var tempText: String
    private lateinit var savePostButton: Button
    private lateinit var realm: Realm
    private lateinit var firestoreHelper:PostFirestoreModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        tempText = intent.getStringExtra("tempString").toString()
        temperatureTextView = findViewById(R.id.tempTextView)

        postEditText = findViewById(R.id.post_EditText)
        savePostButton = findViewById(R.id.uploadPost)

        setupUI()

        savePostButton.setOnClickListener {
            saveDataToFirestore()
        }
    }


    private fun saveDataToFirestore (){
        try{

            firestoreHelper = PostFirestoreModel()

            val db = Firebase.firestore
            val task = PostFirestore()
            task.temp = tempText
            task.diaryInput = postEditText.text.toString()

            firestoreHelper.addToFirestore(task, db)

            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }catch (e:Exception){
            Toast.makeText(this,"Failure",Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupUI() {
        val temp = tempText
        val concatenatedTempText = "Stockholm $temp ℃"
        temperatureTextView.text = concatenatedTempText
    }
}