package com.example.loginsignup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class MyAdapter(
    context: Context,
    private val userArrayList: ArrayList<Usernews>
) : ArrayAdapter<Usernews>(context, R.layout.listviewdesign, userArrayList) {

    private class ViewHolder(view: View) {
        val image: CircleImageView = view.findViewById(R.id.profile_image)
        val name: TextView = view.findViewById(R.id.textview1)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listviewdesign, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentItem = userArrayList[position]
        viewHolder.name.text = currentItem.name
        viewHolder.image.setImageResource(currentItem.imageid)

        return view
    }
}
