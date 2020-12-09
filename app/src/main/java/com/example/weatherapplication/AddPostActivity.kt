package com.example.weatherapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import io.realm.Realm
import io.realm.kotlin.where
import kotlin.math.roundToInt

class AddPostActivity : AppCompatActivity() {
    private var isNew: Boolean = false
    private var existingPostId = 0
    private var existingPostText: String = ""
    private lateinit var temperatureTextView: TextView
    private lateinit var postEditText: EditText
    private var tempText: Double = 0.0
    private lateinit var savePostButton: Button
    private lateinit var deletePostButton: Button
    private lateinit var updatePostButton: Button
    private lateinit var realm: Realm

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        isNew = intent.getBooleanExtra("isNew", false)
        existingPostId = intent.getIntExtra("intentPostId", 0)
        existingPostText = intent.getStringExtra("existingPostText").toString()

        realm = Realm.getDefaultInstance()
        tempText = intent.getDoubleExtra("tempString", 0.0)
        temperatureTextView = findViewById(R.id.tempTextView)
        postEditText = findViewById(R.id.post_EditText)
        savePostButton = findViewById(R.id.uploadPost)
        deletePostButton = findViewById(R.id.delete_button)
        updatePostButton = findViewById(R.id.save_button)

        setupUI()

        savePostButton.setOnClickListener {
            uploadPostToDB(1)
        }
        deletePostButton.setOnClickListener {
            deleteFromRealmWithId(existingPostId)
        }
        updatePostButton.setOnClickListener {
            uploadPostToDB(3)
        }

    }

    private fun deleteFromRealmWithId(id: Int) {
        try {
            val matchingObject = realm.where<Post>().equalTo("id", id).findAll()
            realm.beginTransaction()
            matchingObject.deleteAllFromRealm()
            realm.commitTransaction()
            Toast.makeText(this, "Post Deleted", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        } catch (e: Exception) {
            // Retry ??
            Toast.makeText(this, "Failed to delete post: $e", Toast.LENGTH_SHORT).show()
        }

    }

    private fun uploadPostToDB(choice: Int) {
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
            //val currentDate = LocalDate.now()
            //post.date = currentDate
            if (choice == 1) {
                post.id = nextID
            } else if (choice == 3) {
                post.id = existingPostId
            }

            post.text = postEditText.text.toString()

            realm.copyToRealmOrUpdate(post)
            realm.commitTransaction()

            Toast.makeText(this, "Post uploaded successfully", Toast.LENGTH_SHORT).show()
            val allPosts = realm.where(Post::class.java).findAll()
            allPosts.forEach { post ->
                println("!!! Post: ${post.id}, ${post.text}")
            }
            startActivity(Intent(this, MainActivity::class.java))
        } catch (e: Exception) {
            // Retry??
            Toast.makeText(this, "Failed to upload post: $e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        val temp = tempText.roundToInt()
        val concatenatedTempText = "Stockholm $temp ℃"
        temperatureTextView.text = concatenatedTempText
        if (isNew) {
            savePostButton.isVisible = false
            deletePostButton.text = "Delete"
            updatePostButton.text = "Save"
            postEditText.setText(existingPostText)
        } else {
            deletePostButton.isVisible = false
            updatePostButton.isVisible = false
        }
    }

}