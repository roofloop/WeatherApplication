package com.example.weatherapplication

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import io.realm.Realm
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class AddPostActivity : AppCompatActivity() {
    private lateinit var temperatureTextView: TextView
    private lateinit var postEditText: EditText
    private lateinit var tempText: String
    private lateinit var savePostButton: Button
    private lateinit var realm: Realm

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
            uploadPostToDB()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadPostToDB() {
        try {
            realm.beginTransaction()
            val currentIdNumber: Number? = realm.where(Post::class.java).max("id")
            val nextID: Int
            nextID = if(currentIdNumber == null) {
                1
            } else {
                currentIdNumber.toInt() + 1
            }

            val post = Post()
            val currentDate = LocalDate.now()
            //post.date = currentDate
            post.id = nextID
            post.text = postEditText.text.toString()

            realm.copyToRealmOrUpdate(post)
            realm.commitTransaction()

            Toast.makeText(this, "Post uploaded successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to upload post: $e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        val temp = tempText as String
        val concatenatedTempText = "Stockholm $temp ℃"
        temperatureTextView.text = concatenatedTempText
    }
}