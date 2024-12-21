package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Bookmark : AppCompatActivity() {

    private lateinit var myRecyclerView: RecyclerView
    private val newsArrayList = arrayListOf<book>()
    private lateinit var myAdapterBookmark: MyAdapterBookmark
    private val db = FirebaseFirestore.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
       var refresh: SwipeRefreshLayout?=null
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bookmark)

        myRecyclerView = findViewById(R.id.recyclerview2)
        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myAdapterBookmark = MyAdapterBookmark(newsArrayList, this) { position ->
            handleBookmarkClick(position)
        }
        myRecyclerView.adapter = myAdapterBookmark

        refresh=findViewById(R.id.swipeRefreshLayout2)
        refresh?.setOnRefreshListener {
            fetchBookmarks()
        }
        fetchBookmarks()
    }

    private fun handleBookmarkClick(position: Int) {
        val clickedBookmark = newsArrayList[position]
        Toast.makeText(this, "Clicked: ${clickedBookmark.title}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Newsdetail::class.java).apply {
            putExtra("title", clickedBookmark.title)
            putExtra("description", clickedBookmark.description)
            putExtra("image", clickedBookmark.ImageUrl)
            putExtra("url", clickedBookmark.Newsurl)
            putExtra("position", position)
            putExtra("like", "delete")
            putExtra("date",clickedBookmark.date)
            putExtra("author",clickedBookmark.author)

        }
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchBookmarks() {
        refresh?.isRefreshing=true
        coroutineScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                Toast.makeText(this@Bookmark, "User not logged in", Toast.LENGTH_SHORT).show()
                return@launch
            }

            try {
                val snapshot = currentUser.email?.let {
                    db.collection("User")
                        .document(it)
                        .collection("data")
                        .get()
                        .await()
                }

                newsArrayList.clear()

                if (snapshot != null) {
                    for (document in snapshot.documents) {
                        val news = book().apply {
                            title = document.getString("title") ?: "Unknown Title"
                            Newsurl = document.getString("url") ?: ""
                            ImageUrl = document.getString("image") ?: ""
                            description = document.getString("description") ?: ""
                            date = document.getString("date") ?:"5/5/2023"
                            author=document.getString("author")?:"Gujjar"
                        }
                        newsArrayList.add(news)
                    }
                }
                myAdapterBookmark.notifyDataSetChanged()

            } catch (e: Exception) {
                Toast.makeText(this@Bookmark, "Failed to load bookmarks: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
        Handler().postDelayed({
            refresh?.isRefreshing=false
        },2000)
    }
}

