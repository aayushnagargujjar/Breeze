package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.loginsignup.databinding.ActivityNewsdetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ImageButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Newsdetail : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var binding: ActivityNewsdetailBinding? = null
   var refresh: SwipeRefreshLayout?=null
    private var isLiked = false
    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewsdetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bookmark:ImageButton  = findViewById(R.id.bookmark)


        refresh=findViewById(R.id.swipeRefreshLayout3)
        refresh?.setOnRefreshListener {
            fetchBookmarks()
        }

        fetchBookmarks()
    }

    @SuppressLint("SetTextI18n")
    fun fetchBookmarks() {
        refresh?.isRefreshing = true
        val mail = intent.getStringExtra("mail")?.replace(".", ",").toString()
        val newsdetailtitle = intent.getStringExtra("title") ?: "No Title"
        val newsdetaildescription = intent.getStringExtra("description") ?: "No Description"
        val newsdetailimage = intent.getStringExtra("image") ?: ""
        val newsdetailurl = intent.getStringExtra("url") ?: ""
        val like = intent.getStringExtra("like")
        val author = intent.getStringExtra("author")
        val date = intent.getStringExtra("date")
        val postion = intent.getIntExtra("position", 0)

        binding?.apply {
            newsContent.text = newsdetaildescription
            newstitle.text = newsdetailtitle
            newsAuthor.text = "Published by $author"
            newsDate.text = date
            Glide.with(this@Newsdetail)
                .load(newsdetailimage)
                .error(R.drawable.baseline_person_24)
                .centerCrop()
                .into(newsimage)
            Webviewbutton.setOnClickListener {
                val intent = Intent(this@Newsdetail, Webview::class.java).apply {
                    putExtra("url", newsdetailurl)
                }
                startActivity(intent)
            }

            if (like == "delete") {
                bookmark.setImageResource(R.drawable.redheart)
                bookmark.setOnClickListener {
                    val snewsdetailtitle = newsdetailtitle.trim()
                    val snewsdetaildescription = newsdetaildescription.trim()
                    val snewsdetailimage = newsdetailimage.trim()
                    val snewsdetailurl = newsdetailurl.trim()

                    val usermap = hashMapOf(
                        "title" to snewsdetailtitle,
                        "description" to snewsdetaildescription,
                        "image" to snewsdetailimage,
                        "url" to snewsdetailurl,
                        "date" to date,
                        "author" to author
                    )

                    val userEmail = FirebaseAuth.getInstance().currentUser?.email
                    if (userEmail != null) {
                        db.collection("User").document(userEmail)
                            .collection("data").get().addOnSuccessListener { docref ->
                                if (postion in 0 until docref.documents.size) {
                                    val documentId = docref.documents[postion].id
                                    db.collection("User").document(userEmail)
                                        .collection("data").document(documentId)
                                        .delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this@Newsdetail,
                                                "Document successfully deleted!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent =
                                                Intent(this@Newsdetail, Newsstart::class.java)
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                this@Newsdetail,
                                                "Error deleting document: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Log.w("Firestore", "Error deleting document", e)
                                        }
                                } else {
                                    Toast.makeText(
                                        this@Newsdetail,
                                        "Invalid position",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this@Newsdetail,
                                    "Error getting documents: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.w("Firestore", "Error getting documents", e)
                            }
                    } else {
                        Toast.makeText(
                            this@Newsdetail,
                            "User is not authenticated",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                val snewsdetailtitle = newsdetailtitle.trim()
               checkBookmark(snewsdetailtitle)
                bookmark.setOnClickListener {

                    val snewsdetailtitle = newsdetailtitle.trim()
                    val snewsdetaildescription = newsdetaildescription.trim()
                    val snewsdetailimage = newsdetailimage.trim()
                    val snewsdetailurl = newsdetailurl.trim()

                    val usermap = hashMapOf(
                        "title" to snewsdetailtitle,
                        "description" to snewsdetaildescription,
                        "image" to snewsdetailimage,
                        "url" to snewsdetailurl
                    )
                    val userEmail = FirebaseAuth.getInstance().currentUser?.email
                    if (userEmail != null) {
                        db.collection("User").document(userEmail)
                            .collection("data").document(newsdetailtitle).set(usermap)
                            .addOnSuccessListener {
                                bookmark.setImageResource(R.drawable.redheart)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this@Newsdetail,
                                    "Error writing document: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.w("Firestore", "Error writing document", e)
                            }
                    } else {
                        Toast.makeText(
                            this@Newsdetail,
                            "User is not authenticated",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        Handler().postDelayed({
            refresh?.isRefreshing = false
        }, 2000)
    }



    private fun checkBookmark(newsdetailtitle: String) {
        val snewsdetailtitle = newsdetailtitle.trim()
        val userEmail = FirebaseAuth.getInstance().currentUser?.email

        CoroutineScope(Dispatchers.Main).launch {
            val refresh = userEmail?.let { email ->
                db.collection("User").document(email)
                    .collection("data").document(snewsdetailtitle).get().await()
            }

            val string = refresh?.getString("title") ?: "Unknown Title"

            if (string != snewsdetailtitle) {
                binding?.bookmark?.setImageResource(R.drawable.whiteheart)
            } else {
                binding?.bookmark?.setImageResource(R.drawable.redheart)
            }
        }
    }

}


