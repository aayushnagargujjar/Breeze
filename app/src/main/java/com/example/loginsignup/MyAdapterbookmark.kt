package com.example.loginsignup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MyAdapterBookmark(
    private var newsArrayList: ArrayList<book>,
    private val context: Context,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<MyAdapterBookmark.MyViewHolder>() {

    // ViewHolder for RecyclerView
    class MyViewHolder(itemView: View, ) : RecyclerView.ViewHolder(itemView) {
        val titleImage: ImageView = itemView.findViewById(R.id.headlineimageview)
        val headline: TextView = itemView.findViewById(R.id.newsheadline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclearviewdesign, parent, false)
        return MyViewHolder(itemView, )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newsArrayList[position]


        Glide.with(context)
            .load(currentItem.ImageUrl)
            .placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.baseline_alternate_email_24)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .centerCrop()
            .into(holder.titleImage)


        holder.headline.text = currentItem.title ?: "No Title"


        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return newsArrayList.size
    }


}
