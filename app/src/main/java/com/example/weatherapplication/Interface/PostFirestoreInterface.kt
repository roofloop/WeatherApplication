package com.example.weatherapplication.Interface

import com.example.weatherapplication.Model.PostFirestore
import com.google.firebase.firestore.FirebaseFirestore

interface PostFirestoreInterface {

    fun addToFirestore(postFirestore: PostFirestore, db: FirebaseFirestore)
    fun deleteFromFirestore(id: String, db: FirebaseFirestore)
    fun updateToFirestore(id: String, diaryInput: String, db: FirebaseFirestore)

}