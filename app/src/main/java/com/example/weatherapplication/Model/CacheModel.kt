package com.example.weatherapplication.Model

import android.content.Context
import android.util.Log
import com.example.weatherapplication.Interface.CacheInterface
import java.io.*
import okhttp3.Cache;



class CacheModel : CacheInterface {

    @Throws(IOException::class)
    override fun createCachedFile(context: Context, diaryInputsList: MutableList<PostFirestore>) {

        val outputFile = File(context.cacheDir, "cache").toString() + ".tmp"
        val out = ObjectOutputStream(FileOutputStream(File(outputFile)))

        out.writeObject(diaryInputsList)
        out.close()

    }

    override fun addToCacheFile(context: Context, diaryInputsList: MutableList<PostFirestore>) {

        val diaryListFromCache = readCachedFile(context)

        //OutputStream that we will write our combined list to
        val outputFile = File(context.cacheDir, "cache").toString() + ".tmp"
        val out = ObjectOutputStream(FileOutputStream(File(outputFile)))

        val combinedList = diaryInputsList + diaryListFromCache

        out.writeObject(combinedList)
        out.close()


    }


    override fun readCachedFile(context: Context,): MutableList<PostFirestore> {

        val outputFile = File(context.cacheDir, "cache").toString() + ".tmp"
        val `in` = ObjectInputStream(FileInputStream(File(outputFile)))
        val mutableListObject = `in`.readObject()
        `in`.close()

        return mutableListObject as MutableList<PostFirestore>

    }

    override fun deleteCachedFile(context: Context): Boolean {
        val outputFile = File(context.cacheDir, "")
        Log.d("TAG", "outputFile PATH:  $outputFile")

        return outputFile.deleteRecursively()
    }


}