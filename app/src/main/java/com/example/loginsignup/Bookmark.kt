package com.example.loginsignup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Bookmark : Fragment(R.layout.fragment_bookmark) {

    private lateinit var viewModel: BookmarkViewModel
    private lateinit var myRecyclerView: RecyclerView
    private val newsArrayList = arrayListOf<book>()
    private lateinit var myAdapterBookmark: MyAdapterBookmark
    private val db = FirebaseFirestore.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private var refresh: SwipeRefreshLayout? = null

    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(BookmarkViewModel::class.java)

        myRecyclerView = view.findViewById(R.id.recyclerview2)
        myRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        myAdapterBookmark = MyAdapterBookmark(newsArrayList, requireContext()) { position ->
            handleBookmarkClick(position)
        }

        myRecyclerView.adapter = myAdapterBookmark
        refresh = view.findViewById(R.id.swipeRefreshLayout2)
        refresh?.setOnRefreshListener {
            fetchBookmarks()
        }

        fetchBookmarks()
    }

    private fun handleBookmarkClick(position: Int) {
        val clickedBookmark = newsArrayList[position]
        viewModel.clickedBookmark = clickedBookmark
        val bundle = Bundle().apply {
            putString("News_bookmark", "Bookmark")
        }

        val fragment = NewsDetailFragment().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            addToBackStack(null)
            commit()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchBookmarks() {
        refresh?.isRefreshing = true
        coroutineScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                refresh?.isRefreshing = false
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

                snapshot?.documents?.forEach { document ->
                    val news = book().apply {
                        title = document.getString("title") ?: "Unknown Title"
                        Newsurl = document.getString("url") ?: ""
                        ImageUrl = document.getString("image") ?: ""
                        description = document.getString("description") ?: ""
                        date = document.getString("date") ?: "5/5/2023"
                        author = document.getString("author") ?: "Gujjar"
                    }
                    newsArrayList.add(news)
                }

                myAdapterBookmark.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Failed to load bookmarks: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                refresh?.isRefreshing = false
            }
        }
    }
}

class BookmarkViewModel : ViewModel() {
    var clickedBookmark: book? = null
    var Breezenewsclicked: News? = null
}
