package com.example.weatherapplication.Model

import android.content.Context
import android.util.Log
import com.example.weatherapplication.Interface.CacheInterface
import java.io.*


class CacheModel : CacheInterface {

    @Throws(IOException::class)
    override fun createCachedFile(context: Context, diaryInputsList: MutableList<PostFirestore>) {

        val outputFile = File(context.cacheDir, "cache").toString() + ".tmp"
        val out = ObjectOutputStream(FileOutputStream(File(outputFile)))

        out.writeObject(diaryInputsList)
        out.close()

    }

    override fun addToCacheFile(context: Context, diaryInput: PostFirestore) {

        val diaryListFromCache = readCachedFile(context)
        val c = diaryListFromCache.size
        println("!!! CM From Cache: $c")

        //OutputStream that we will write our combined list to
        val outputFile = File(context.cacheDir, "cache").toString() + ".tmp"
        val out = ObjectOutputStream(FileOutputStream(File(outputFile)))

        diaryListFromCache.add(diaryInput)
        val combinedList: MutableList<PostFirestore> = diaryListFromCache

        val p = combinedList.size
        println("!!!CM CombinedList: $p")

        out.writeObject(combinedList)
        out.close()


    }


    override fun readCachedFile(context: Context,): MutableList<PostFirestore> {

        val outputFile = File(context.cacheDir, "cache").toString() + ".tmp"
        val `in` = ObjectInputStream(FileInputStream(File(outputFile)))
        val mutableListObject = `in`.readObject()
        `in`.close()
        val m = mutableListObject as MutableList<PostFirestore>
        val mm = m.size
        println("!!! readCache: $mm")

        return mutableListObject as MutableList<PostFirestore>

    }

    override fun deleteCachedFile(context: Context): Boolean {
        val outputFile = File(context.cacheDir, "")
        Log.d("TAG", "outputFile PATH:  $outputFile")

        return outputFile.deleteRecursively()
    }


}