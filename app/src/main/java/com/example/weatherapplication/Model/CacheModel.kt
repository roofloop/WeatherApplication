package com.example.weatherapplication.Model

import android.content.Context
import android.util.Log
import com.example.weatherapplication.Interface.CacheInterface
import java.io.*


class CacheModel : CacheInterface {

    @Throws(IOException::class)
    override fun createCachedFile(context: Context, diaryInputsList: MutableList<PostFirestore>) {
        val diaryInputsListFinal = mutableListOf<PostFirestore>()

        val outputFile = File(context.cacheDir, "cache").toString() + ".tmp"
        val out = ObjectOutputStream(FileOutputStream(File(outputFile)))

        val sortedList = SortingFunctions.dateInsertionSorting(diaryInputsList)
        for (count in 0 until sortedList.count()) {
            if (count <= 3) {
                diaryInputsListFinal.add(sortedList[count])
            }
        }

        out.writeObject(diaryInputsListFinal)
        out.close()

    }

    override fun addToCacheFile(context: Context, diaryInput: PostFirestore) {
        val diaryInputsListFinal = mutableListOf<PostFirestore>()

        val diaryListFromCache = readCachedFile(context)
        val c = diaryListFromCache.size
        println("!!! CM From Cache: $c")

        //OutputStream that we will write our combined list to
        val outputFile = File(context.cacheDir, "cache").toString() + ".tmp"
        val out = ObjectOutputStream(FileOutputStream(File(outputFile)))

        diaryListFromCache.add(diaryInput)
        val sortedList = SortingFunctions.dateInsertionSorting(diaryListFromCache)
        for (count in 0 until sortedList.count()) {
            if (count <= 3) {
                diaryInputsListFinal.add(sortedList[count])
            }
        }

        out.writeObject(diaryInputsListFinal)
        out.close()

    }


    override fun readCachedFile(context: Context): MutableList<PostFirestore> {

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