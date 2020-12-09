package com.example.weatherapplication.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.weatherapplication.Interface.PostModel
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.Model.Post
import com.example.weatherapplication.R
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_selected.*
import java.lang.Exception
private lateinit var helper:PostModel
private lateinit var realm: Realm



class SelectedActivity : AppCompatActivity() {
    var realmId:Int? = 0
    var realmText:String? = ""
    var realmTemp:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected)

        realm = Realm.getDefaultInstance()

        realmId = intent.getIntExtra("realmId",0)
        realmText = intent.getStringExtra("realmText")
        realmTemp = intent.getStringExtra("realmTemp")

        textViewId.text = realmId.toString()
        textViewEdit.text = realmText
        textViewTemp.text = realmTemp

        val deleteButton = findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener { deletePost() }

    }

    private fun deletePost() {
        try {
            helper = PostModel()
            //saving to Database
            helper.deletePost(realm, realmId!!)
            Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } catch (e: Exception){
            Toast.makeText(this,"Failure", Toast.LENGTH_SHORT).show()
        }
    }
}