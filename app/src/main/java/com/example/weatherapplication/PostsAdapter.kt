package com.example.weatherapplication

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kotlinx.android.synthetic.main.post_rv_layout.*
import kotlinx.android.synthetic.main.post_rv_layout.view.*
import java.text.Normalizer
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class PostsAdapter(private val context: Context?, private val postList: RealmResults<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.post_rv_layout, parent, false)
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //val dateTextStringified = postList[position]?.date?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        //  .toString()
        holder.itemView.post_cell_date_text.text = position.toString()
    }

    override fun getItemCount(): Int {
        return postList.count()
    }

    class ViewHolder(v: View?): RecyclerView.ViewHolder(v!!) {
        val dateText = itemView.findViewById<TextView>(R.id.post_cell_date_text)
    }


}