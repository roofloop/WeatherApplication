package com.example.weatherapplication.Interface


import android.content.Context
import android.util.Log
import com.example.weatherapplication.Model.CacheModel
import com.example.weatherapplication.Model.PostFirestore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import javax.security.auth.callback.Callback


class PostFirestoreModel : PostFirestoreInterface {

    private val TAG: String = "PostFirestoreModel"
    private val db = Firebase.firestore
    private val cacheHelper: CacheModel = CacheModel()
    private val settings = firestoreSettings {
        isPersistenceEnabled = false
    }

    override fun getFromFirestore(context: Context, callback: (MutableList<PostFirestore>) -> Unit) {
        db.firestoreSettings = settings
        val notesList = mutableListOf<PostFirestore>()
        db.collection("InputsDiary")
                .addSnapshotListener { snapshot, e ->
                    notesList.clear()

                    if (snapshot != null && !snapshot.isEmpty) {
                        for (doc in snapshot.documents) {
                            val note = doc.toObject(PostFirestore::class.java)
                            notesList.add(note!!)
                            cacheHelper.createCachedFile(context, notesList)
                        }

                        callback(notesList)


                    } else {
                        //Refreshing the RV and cache if firestore is empty.
                        cacheHelper.deleteCachedFile(context)
                        callback(notesList)
                    }
                }

    }

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