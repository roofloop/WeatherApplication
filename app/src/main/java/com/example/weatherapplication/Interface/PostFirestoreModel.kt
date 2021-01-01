package com.example.weatherapplication.Interface


import android.content.Context
import android.util.Log
import com.example.weatherapplication.Model.CacheModel
import com.example.weatherapplication.Model.PostFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase


class PostFirestoreModel : PostFirestoreInterface {

    private val TAG: String = "PostFirestoreModel"
    private val db = Firebase.firestore
    private val cacheHelper: CacheModel = CacheModel()
    private val settings = firestoreSettings {
        isPersistenceEnabled = false
    }

    override fun getFromFirestore(context: Context, callback: (MutableList<PostFirestore>) -> Unit) {
        val diaryInputsList = mutableListOf<PostFirestore>()
        try {
            db.collection("DiaryInputs")
                    .addSnapshotListener { snapshot, e ->
                        diaryInputsList.clear()

                        if (snapshot != null && !snapshot.isEmpty) {
                            for (doc in snapshot.documents) {
                                val note = doc.toObject(PostFirestore::class.java)
                                diaryInputsList.add(note!!)
                            }


                           val cacheList = cacheHelper.readCachedFile(context)

                            val finalList = ArrayList<Any>()
                            finalList.addAll(cacheList)
                            finalList.addAll(diaryInputsList)

                            cacheHelper.createCachedFile(context, finalList as MutableList<PostFirestore>)

                            
                            callback(diaryInputsList)

                        } else {
                            //Refreshing the RV and cache if firestore is empty.
                            cacheHelper.deleteCachedFile(context)
                            callback(diaryInputsList)
                        }
                    }
        } catch (e: Exception){
            Log.d(TAG, "Failure", e)
        }

    }

    override fun addToFirestore(postFirestore: PostFirestore) {

        try {
            val newDiaryInputRef = db.collection("DiaryInputs").document()
            postFirestore.id = newDiaryInputRef.id

            // Add a new document with a generated ID
            newDiaryInputRef
                .set(postFirestore)
                .addOnSuccessListener {

                    Log.d(TAG, "DocumentSnapshot added with ID:" + newDiaryInputRef.id)

                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

        } catch (e: Exception){
            Log.w(TAG, "Error adding to firestore", e)
        }
    }
    override fun deleteFromFirestore(id: String) {

        db.collection("DiaryInputs").document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
            }
    }

    override fun updateToFirestore(id: String, diaryInput: String) {
        val updateDiaryInputRef = db.collection("DiaryInputs").document(id)

        updateDiaryInputRef
            .update("diaryInput", diaryInput)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

    }
}