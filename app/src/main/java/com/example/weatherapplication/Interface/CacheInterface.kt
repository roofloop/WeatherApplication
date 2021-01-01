package com.example.weatherapplication.Interface

import android.content.Context
import com.example.weatherapplication.Model.PostFirestore
import java.io.IOException

interface CacheInterface {

    fun createCachedFile(context: Context, notesList: MutableList<PostFirestore>)
    @Throws(IOException::class, ClassNotFoundException::class)
    fun readCachedFile(context: Context): MutableList<PostFirestore>
    fun deleteCachedFile(context: Context): Boolean
    fun addToCacheFile(task: PostFirestore)
}