package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.loginsignup.databinding.ActivityNewsdetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Newsdetail : AppCompatActivity() {

    // Initialize Firestore
    private val db = FirebaseFirestore.getInstance()
    private var binding: ActivityNewsdetailBinding? = null

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
        val bookmark=findViewById<TextView>(R.id.bookmark)
        val mail= intent.getStringExtra("mail")?.replace(".",",").toString()
        val newsdetailtitle = intent.getStringExtra("title") ?: "No Title"
        val newsdetaildescription = intent.getStringExtra("description") ?: "No Description"
        val newsdetailimage = intent.getStringExtra("image") ?: ""
        val newsdetailurl = intent.getStringExtra("url") ?: ""
        val like=intent.getStringExtra("like")
        val postion=intent.getIntExtra("position",0)

        binding?.apply {
            newsContent.text = newsdetaildescription
            newstitle.text = newsdetailtitle
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
            if(like=="delete"){
                binding?.bookmark?.text="Delete"
                binding?.bookmark?.setOnClickListener {
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
                    val userid = FirebaseAuth.getInstance().currentUser?.uid?.replace(".",",")
                 db.collection("User").document("userid")
                .collection("data").get().addOnSuccessListener { docref ->
                            if (postion in 0 until docref.documents.size) {
                val documentId = docref.documents[postion].id
                db.collection("User").document("userid")
                    .collection("data").document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@Newsdetail,
                            "Document successfully deleted!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@Newsdetail, Newsstart::class.java)
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
                Toast.makeText(this@Newsdetail, "Invalid position", Toast.LENGTH_SHORT).show()
            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this@Newsdetail, "Error getting documents: ${e.message}", Toast.LENGTH_SHORT).show()
                            Log.w("Firestore", "Error getting documents", e)
      }

                }}
            else{
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
                val userid = FirebaseAuth.getInstance().currentUser?.uid?.replace(".",",")
                val useridk= "${mail}"


                 db.collection("User").document("userid")
                    .collection("data").add(usermap)
                    .addOnSuccessListener {
                       bookmark.text="Bookmarked"
                    }
                    .addOnFailureListener { e ->
                        bookmark.text="No Bookmark"
                        Log.w("Firestore", "Error writing document", e)
                    }
                }

            }}
        }
    }

