package com.example.weatherapplication.RecyclerViewAdapter


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapplication.Model.PostFirestore
import com.example.weatherapplication.R
import kotlinx.android.synthetic.main.post_staggered_rv.view.*

class PostFirestoreAdapter (
    private val firestoreList: MutableList<PostFirestore>)
    : RecyclerView.Adapter<PostFirestoreAdapter.ViewHolder>() {

    var onItemClick: ((PostFirestore) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_staggered_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.stag_temp.text = firestoreList[position].temp
        holder.itemView.stag_text.text = firestoreList[position].diaryInput
        holder.itemView.stag_date.text = firestoreList[position].creationDate

    }

    override fun getItemCount(): Int {
        return firestoreList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(firestoreList[adapterPosition]!!)
            }
        }
    }
}