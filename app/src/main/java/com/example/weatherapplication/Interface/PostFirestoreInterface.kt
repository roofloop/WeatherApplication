package com.example.weatherapplication.Interface

import android.content.Context
import com.example.weatherapplication.Model.PostFirestore
import com.google.firebase.firestore.FirebaseFirestore
import javax.security.auth.callback.Callback

interface PostFirestoreInterface {

    fun getFromFirestore(context: Context, callback: (MutableList<PostFirestore>) -> Unit)
    fun addToFirestore(postFirestore: PostFirestore, db: FirebaseFirestore)
    fun deleteFromFirestore(id: String, db: FirebaseFirestore)
    fun updateToFirestore(id: String, diaryInput: String, db: FirebaseFirestore)

}