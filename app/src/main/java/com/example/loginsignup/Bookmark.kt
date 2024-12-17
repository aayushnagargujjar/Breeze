package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignup.databinding.ActivityBookmarkBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Bookmark : AppCompatActivity() {
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var newsArrayList: ArrayList<book>
    private lateinit var myAdapterBookmark: MyAdapterBookmark
    private val db = FirebaseFirestore.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bookmark)

        // Handle window insets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView
        myRecyclerView = findViewById(R.id.recyclerview2)
        newsArrayList = arrayListOf()
        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myAdapterBookmark = MyAdapterBookmark(newsArrayList, this) { position ->
            // Handle item click
            val clickedBookmark = newsArrayList[position]
            Toast.makeText(this, "Clicked: ${clickedBookmark.title}", Toast.LENGTH_SHORT).show()
            val intent= Intent(this,Newsdetail::class.java).apply {
                putExtra("title",clickedBookmark.title)
                putExtra("description",clickedBookmark.description)
                putExtra("image",clickedBookmark.ImageUrl)
                putExtra("url",clickedBookmark.Newsurl)
                putExtra("position",position)
                putExtra("like","delete")
            }
            startActivity(intent)
        }
        myRecyclerView.adapter = myAdapterBookmark

        // Fetch data from Firestore
        fetchBookmarks()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchBookmarks() {

        coroutineScope.launch {
            try {
                // Fetch Firestore data
                val userid= FirebaseAuth.getInstance().currentUser?.uid?.replace(".",",")
            db.collection("User")
                    .document("userid")
                    .collection("data")
                    .get().addOnSuccessListener {result->

                        // Clear the list to avoid duplication
                        newsArrayList.clear()

                        //  Map Firestore documents to book objects and add to the list
                        for (document in result.documents) {
                            val news = book().apply {
                                title = document.getString("title") ?: "No Title"
                 Newsurl = document.getString("url") ?: "No URL"
                                ImageUrl = document.getString("image") ?: "No Image URL"
                                description = document.getString("description") ?: "No Description"
                            }
                            newsArrayList.add(news)
                        }

                        // Notify the adapter that the data set has changed
           myAdapterBookmark.notifyDataSetChanged()

                        // Success message
                        Toast.makeText(this@Bookmark, "Bookmarks loaded successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{
                        Toast.makeText(this@Bookmark, "Failed to load bookmarks", Toast.LENGTH_SHORT).show()
                    }


            }
            catch (e: Exception) {
                // Error message
                Toast.makeText(this@Bookmark, "Failed to load bookmarks: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
