package com.example.weatherapplication.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.weatherapplication.Interface.PostFirestoreModel
import com.example.weatherapplication.NetworkUtils
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

    private lateinit var deleteButton: Button
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected)

        id = intent.getStringExtra("id")
        diaryInput = intent.getStringExtra("diaryInput")
        temp = intent.getStringExtra("temp")

        deleteButton = findViewById(R.id.deleteButton)
        updateButton = findViewById(R.id.saveButton)

        textViewEditText.setText(diaryInput.toString())
        textViewTemp.text = temp

        handleNetworkChanges()

    }

    private fun handleNetworkChanges()
    {

        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, { isConncted ->
            if (!isConncted)
            {
                deleteButton.setOnClickListener { Toast.makeText(this, "Network is missing", Toast.LENGTH_SHORT).show() }
                updateButton.setOnClickListener { Toast.makeText(this, "Network is missing", Toast.LENGTH_SHORT).show() }

            }else{
                deleteButton.setOnClickListener { deleteFromFirestore() }
                updateButton.setOnClickListener { updateFirestore() }

            }
        })
    }


    private fun deleteFromFirestore(){
        val db = Firebase.firestore

        firestoreHelper = PostFirestoreModel()
        firestoreHelper.deleteFromFirestore(id!!)
        Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
        finish()

    }

    private fun updateFirestore(){
        val db = Firebase.firestore

        firestoreHelper = PostFirestoreModel()

        firestoreHelper.updateToFirestore(id!!,textViewEditText.text.toString())

        Log.d(TAG, "textViewEditText: " + textViewEditText.text.toString())
        Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()

        finish()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}