package com.example.weatherapplication.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.weatherapplication.Activity.MainActivity
import com.example.weatherapplication.Model.LoginUtility
import com.google.firebase.auth.FirebaseAuth
import com.example.weatherapplication.R
import com.google.firebase.FirebaseApp

private lateinit var auth: FirebaseAuth

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val userEmail = findViewById<EditText>(R.id.userEmail)
        val userPass = findViewById<EditText>(R.id.userPassword)
        val logInButton = findViewById<Button>(R.id.button_sign_in)
        val forgotPasswordButton = findViewById<TextView>(R.id.forgot_password_textview)

        // Initialize Firebase Auth and see if user already signed in
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        auth.signOut()

        if (user != null) {
            // User is signed in
            Log.d("user", "Signed in")


            val intent = Intent(this, MainActivity::class.java).apply {
            }

            startActivity(intent)
            finish()

        } else {
            // No user is signed in
            Log.d("user", "Not signed in")

        }

        // OnclickListener for the button loginButton
        logInButton.setOnClickListener {

            if(LoginUtility.validateLoginInput(userEmail.text.toString(), userPass.text.toString())) {
                // Sign in with the typed in credentials
                auth.signInWithEmailAndPassword(
                        userEmail.text.toString(),
                        userPass.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success")
                            val user = auth.currentUser

                            // Do something in response to button
                            val intent = Intent(this, MainActivity::class.java).apply {
                            }

                            startActivity(intent)
                            finish()


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                    baseContext, "Authentication failed. Wrong credentials",
                                    Toast.LENGTH_SHORT
                            ).show()

                        }

                    }
            } else {
                Toast.makeText(
                        baseContext, "Please type something",
                        Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}
