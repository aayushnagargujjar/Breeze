package com.example.loginsignup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TopicAdapter(private var topicList: ArrayList<Topic>, private val context: Context)
    : RecyclerView.Adapter<TopicAdapter.MyViewHolder>() {

    private lateinit var myListener: OnItemClickListener

    class MyViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val headline: TextView = itemView.findViewById(R.id.topictext)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.searchviewdesign, parent, false)
        return MyViewHolder(itemView, myListener)
    }

    override fun getItemCount(): Int {
        return topicList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = topicList[position]
        holder.headline.text = currentItem.name

        if (currentItem.isSelected) {
            holder.headline.setTextColor(ContextCompat.getColor(context, R.color.reds))
            val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
            holder.headline.startAnimation(animation)
            holder.headline.postDelayed({
                holder.headline.clearAnimation()
            }, 3000)
        } else {
            holder.headline.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        }

        holder.itemView.setOnClickListener {

            topicList.forEach { it.isSelected = false }
            currentItem.isSelected = true
            notifyDataSetChanged()

            myListener.onItemClick(position)
        }
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
