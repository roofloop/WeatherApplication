package com.example.weatherapplication.Model

import android.content.Context
import android.util.Log
import com.example.weatherapplication.Interface.CacheInterface
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class CacheModel : CacheInterface {

    @Throws(IOException::class)
    override fun createCachedFile(context: Context, notesList: MutableList<PostFirestore>) {

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())


        val outputFile = File(context.cacheDir, "cache").toString() + ".tmp"

        var size: Long = 0
        val cacheDirectory = context.cacheDir

        val files: Array<File> = cacheDirectory.listFiles()!!
        for (f in files) {
            size += f.length()
        }

        Log.d("TAG", "Cache size:  $size")


        val out: ObjectOutput = ObjectOutputStream(
                FileOutputStream(
                        File(outputFile)
                )
        )
        out.writeObject(notesList)
        Log.d("TAG", "notesList in cache:  $notesList")
        out.close()
    }

    override fun readCachedFile(context: Context): MutableList<PostFirestore> {

        val outputFile = File(context.cacheDir, "cache").toString() + ".tmp"

        val `in` = ObjectInputStream(
                FileInputStream(File(outputFile))
        )
        val mutableListObject = `in`.readObject()
        Log.d("TAG", "CACHE listObject $mutableListObject")
        `in`.close()

        return mutableListObject as MutableList<PostFirestore>
    }

    override fun deleteCachedFile(context: Context): Boolean {
        val outputFile = File(context.cacheDir, "")
        Log.d("TAG", "outputFile PATH:  $outputFile")

        return outputFile.deleteRecursively()
    }
}