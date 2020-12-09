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
import com.example.weatherapplication.Interface.PostModel
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.Model.Post
import com.example.weatherapplication.R
import io.realm.Realm
import java.lang.Exception
import java.time.LocalDate

class AddPostActivity : AppCompatActivity() {
    private lateinit var temperatureTextView: TextView
    private lateinit var postEditText: EditText
    private lateinit var tempText: String
    private lateinit var savePostButton: Button
    private lateinit var realm: Realm
    private lateinit var helper:PostModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        realm = Realm.getDefaultInstance()
        tempText = intent.getStringExtra("tempString").toString()
        temperatureTextView = findViewById(R.id.tempTextView)

        postEditText = findViewById(R.id.post_EditText)
        savePostButton = findViewById(R.id.uploadPost)

        setupUI()

        savePostButton.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {

        try {
            helper = PostModel()
            val task = Post()
            task.temp = tempText
            task.text = postEditText.text.toString()

            //saving to Database
            helper.addPost(realm,task)
            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()

            startActivity(Intent(this,MainActivity::class.java))
            finish()

        } catch (e:Exception){
            Toast.makeText(this,"Failure",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        val temp = tempText as String
        val concatenatedTempText = "Stockholm $temp ℃"
        temperatureTextView.text = concatenatedTempText
    }
}