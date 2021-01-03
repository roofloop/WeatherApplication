package com.example.weatherapplication.Interface


import android.content.Context
import android.util.Log
import com.example.weatherapplication.Model.CacheModel
import com.example.weatherapplication.Model.PostFirestore
import com.example.weatherapplication.Model.SortingFunctions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import java.io.FileNotFoundException


class PostFirestoreModel : PostFirestoreInterface {

    private val TAG: String = "PostFirestoreModel"
    private val db = Firebase.firestore
    private val cacheHelper: CacheModel = CacheModel()

    override fun getFromFirestore(context: Context, callback: (MutableList<PostFirestore>) -> Unit) {

        val diaryInputsList = mutableListOf<PostFirestore>()
        val diaryInputsListFinal = mutableListOf<PostFirestore>()

        try {
            db.collection("DiaryInputs")
                    .addSnapshotListener { snapshot, e ->
                        diaryInputsList.clear()

                        if (snapshot != null && !snapshot.isEmpty) {
                            for (doc in snapshot.documents) {
                                val diaryInputs = doc.toObject(PostFirestore::class.java)

                                // Adding data from firestore to out mutableList
                                diaryInputsList.add(diaryInputs!!)
                            }
                            // Creating our cache file (or overwriting existing), with fresh data from firestore.
                            /*
                            val sortedList = SortingFunctions.dateInsertionSorting(diaryInputsList)
                            for (count in 0 until sortedList.count()) {
                                if (count <= 1) {
                                    diaryInputsListFinal.add(sortedList[count])
                                }
                            }


                            cacheHelper.createCachedFile(context, diaryInputsListFinal)
                            */
                            // Returning the up to date mutableList
                            callback(diaryInputsList)

                        } else {
                            //Refreshing the RV and deleting the cache if firestore is empty.
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