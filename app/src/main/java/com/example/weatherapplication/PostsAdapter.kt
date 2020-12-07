package com.example.weatherapplication

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kotlinx.android.synthetic.main.post_rv_layout.*
import kotlinx.android.synthetic.main.post_rv_layout.view.*
import java.text.Normalizer
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class PostsAdapter(private val context: Context, private val postList: RealmResults<Post>, var clickListener: OnPostClickListener) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.post_rv_layout, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val dateTextStringified = postList[position]?.date?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        //  .toString()
        //val post = postList[position]
        //holder.dateText.text = post?.id.toString()
        postList.get(position)?.let { holder.initialize(it, clickListener) }
    }

    override fun getItemCount(): Int {
        return postList.count()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var dateText = itemView.findViewById<TextView>(R.id.post_cell_date_text)

        fun initialize(post: Post, action: OnPostClickListener) {
            dateText.text = post.text

            itemView.setOnClickListener {
                action.onItemClick(post, adapterPosition)
            }
        }
    }

}

interface OnPostClickListener {
    fun onItemClick(item: Post, position: Int)
}