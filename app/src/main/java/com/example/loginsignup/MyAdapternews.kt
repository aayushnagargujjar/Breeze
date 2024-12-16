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

class MyAdapterNews(private val newsArrayList: ArrayList<News>, private val context: Context)
    : RecyclerView.Adapter<MyAdapterNews.MyViewHolder>() {

    private lateinit var myListener: OnItemClickListener

    class MyViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val titleImage: ImageView = itemView.findViewById(R.id.headlineimageview)
        val headline: TextView = itemView.findViewById(R.id.newsheadline)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclearviewdesign, parent, false)
        return MyViewHolder(itemView, myListener)
    }

    override fun getItemCount(): Int {
        return newsArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newsArrayList[position]
        Glide.with(context)
            .load(currentItem.ImageUrl)
            .error(R.drawable.baseline_person_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(holder.titleImage)
        holder.headline.text = currentItem.title
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
