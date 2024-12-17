package com.example.loginsignup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MyAdapterBookmark(
    private var newsArrayList: ArrayList<book>, // Dataset
    private val context: Context,               // Context
    private val onItemClick: (Int) -> Unit      // Lambda for item clicks
) : RecyclerView.Adapter<MyAdapterBookmark.MyViewHolder>() {

    // ViewHolder for RecyclerView
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleImage: ImageView = itemView.findViewById(R.id.headlineimageview)
        val headline: TextView = itemView.findViewById(R.id.newsheadline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclearviewdesign, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newsArrayList[position]

        // Load image using Glide
        Glide.with(context)
            .load(currentItem.ImageUrl)
            .placeholder(R.drawable.baseline_person_24) // Placeholder while loading
            .error(R.drawable.baseline_alternate_email_24)             // Fallback image on error
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // Optimized cache strategy
            .centerCrop()
            .into(holder.titleImage)

        // Set headline text
        holder.headline.text = currentItem.title ?: "No Title"

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return newsArrayList.size
    }

    // Update the dataset with DiffUtil
    fun updateData(newList: ArrayList<book>) {
        val diffCallback = BookmarkDiffCallback(newsArrayList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        newsArrayList.clear()
        newsArrayList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    // DiffUtil callback for better performance
    class BookmarkDiffCallback(
        private val oldList: List<book>,
        private val newList: List<book>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].Newsurl == newList[newItemPosition].Newsurl
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
