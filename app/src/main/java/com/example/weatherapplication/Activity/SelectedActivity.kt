package com.example.weatherapplication.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.weatherapplication.Interface.PostFirestoreModel
import com.example.weatherapplication.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_selected.*



class SelectedActivity : AppCompatActivity() {
    var id:String? = ""
    var diaryInput:String? = ""
    var temp:String? = ""
    private lateinit var firestoreHelper:PostFirestoreModel
    var TAG:String? = "SelectedActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected)

        id = intent.getStringExtra("id")
        diaryInput = intent.getStringExtra("diaryInput")
        temp = intent.getStringExtra("temp")

        textViewEditText.setText(diaryInput.toString())
        textViewTemp.text = temp

        val deleteButton = findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener { deleteFromFirestore() }

        val updateButton = findViewById<Button>(R.id.saveButton)
        updateButton.setOnClickListener { updateFirestore() }

    }

    private fun deleteFromFirestore(){
        val db = Firebase.firestore

        firestoreHelper = PostFirestoreModel()
        firestoreHelper.deleteFromFirestore(id!!,db)

        Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }


    private fun updateFirestore(){
        val db = Firebase.firestore

        firestoreHelper = PostFirestoreModel()

        firestoreHelper.updateToFirestore(id!!,textViewEditText.text.toString(),db  )

        Log.d(TAG, "textViewEditText: " + textViewEditText.text.toString())

        Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

}