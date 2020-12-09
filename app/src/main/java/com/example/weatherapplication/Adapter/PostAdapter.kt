package com.example.weatherapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kotlinx.android.synthetic.main.post_staggered_rv.view.*
import com.example.weatherapplication.Model.Post

class PostAdapter(val context: Context?, private val postList: RealmResults<Post>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Post) -> Unit)? = null


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.stag_temp.text = postList[position]!!.temp
        holder.itemView.stag_text.text = postList[position]!!.text
        holder.itemView.stag_id.text = postList[position]!!.id.toString()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.post_staggered_rv, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return postList.size
    }



  inner class  ViewHolder(view: View):RecyclerView.ViewHolder(view) {


      init {
          itemView.setOnClickListener {
              onItemClick?.invoke(postList[adapterPosition]!!)
          }
      }

      val text = itemView.findViewById<TextView>(R.id.stag_text)
      val id = itemView.findViewById<TextView>(R.id.stag_id)
      val temp = itemView.findViewById<TextView>(R.id.tempTextView)

  }

}