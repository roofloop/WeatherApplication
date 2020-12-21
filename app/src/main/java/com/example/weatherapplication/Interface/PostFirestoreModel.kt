package com.example.weatherapplication.Interface


import android.util.Log
import com.example.weatherapplication.Model.PostFirestore
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception


class PostFirestoreModel : PostFirestoreInterface {

    private val TAG: String = "PostFirestoreModel"

    override fun addToFirestore(postFirestore: PostFirestore, db: FirebaseFirestore) {

        try{

            val newDiaryInputRef = db.collection("InputsDiary").document()
            postFirestore.id = newDiaryInputRef.id

            // Add a new document with a generated ID
            newDiaryInputRef
                .set(postFirestore)
                .addOnSuccessListener {

                    Log.d(TAG, "DocumentSnapshot added with ID:" + newDiaryInputRef.id )

                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

        }catch (e: Exception){
            Log.w(TAG, "Error adding to firestore", e)
        }
    }
    override fun deleteFromFirestore(id: String, db: FirebaseFirestore) {

        db.collection("InputsDiary").document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
            }
    }

    override fun updateToFirestore(id: String, diaryInput: String, db: FirebaseFirestore) {
        val updateDiaryInputRef = db.collection("InputsDiary").document(id)

        updateDiaryInputRef
            .update("diaryInput", diaryInput)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

    }
}