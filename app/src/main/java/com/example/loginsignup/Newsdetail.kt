package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.loginsignup.databinding.FragmentNewsdetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NewsDetailFragment : Fragment(R.layout.fragment_newsdetail) {

    private val db = FirebaseFirestore.getInstance()
    private var binding: FragmentNewsdetailBinding? = null
    private var refresh: SwipeRefreshLayout? = null
    private lateinit var viewModel: BookmarkViewModel
    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsdetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(BookmarkViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
           checkBookmark(viewModel.clickedBookmark?.title.toString())
        refresh = view.findViewById(R.id.swipeRefreshLayout3)
        refresh?.setOnRefreshListener {

        }
        var News_bookamrk = arguments?.getString("News_bookmark")
        if (News_bookamrk == "Bookmark") {
            viewModel.clickedBookmark?.let { bookmark ->
                binding?.apply {
                    newsContent.text = bookmark.description
                    newstitle.text = bookmark.title
                    newsAuthor.text = "Published by ${bookmark.author}"
                    newsDate.text = bookmark.date
                    Glide.with(this@NewsDetailFragment)
                        .load(bookmark.ImageUrl)
                        .error(R.drawable.baseline_person_24)
                        .centerCrop()
                        .into(newsimage)
                    like.setOnClickListener{
                        deleteBookmark()
                    }
                      checkBookmark(newstitle.text.toString())
                    Webviewbutton.setOnClickListener {
                        var bundle = Bundle().apply {
                            putString("url", "${bookmark.Newsurl}")
                        }
                        val fragment = Webview().apply {
                            arguments = bundle
                        }
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.flFragment, fragment)
                            addToBackStack(null)
                            commit()
                        }

                    }
                }
            }
        }
        else {
            viewModel.Breezenewsclicked?.let { News ->
                binding?.apply {
                    newsContent.text = News.description
                    newstitle.text = News.title
                    newsAuthor.text = "Published by ${News.author}"
                    newsDate.text = News.date
                    Glide.with(this@NewsDetailFragment)
                        .load(News.ImageUrl)
                        .error(R.drawable.baseline_person_24)
                        .centerCrop()
                        .into(newsimage)
                       checkBookmark(News.title.toString())
                      like.setOnClickListener {
                          val userEmail = FirebaseAuth.getInstance().currentUser?.email
                          if (userEmail != null) {
                              val usermap = hashMapOf(
                                  "title" to News.title,
                                  "description" to News.description,
                                  "image" to News.ImageUrl,
                                  "url" to News.Newsurl,
                                  "date" to News.date,
                                  "author" to News.author
                              )
                              News.title?.let { it1 ->
                                  db.collection("User").document(userEmail)
                                      .collection("data").document(it1).set(usermap)
                                      .addOnSuccessListener {
                                          binding?.like?.setImageResource(R.drawable.redheart)
                                      }
                                      .addOnFailureListener { e ->
                                          Toast.makeText(
                                              context,
                                              "Error writing document: ${e.message}",
                                              Toast.LENGTH_SHORT
                                          ).show()
                                          Log.w("Firestore", "Error writing document", e)
                                      }
                              }
                          } else {
                              Toast.makeText(
                                  context,
                                  "User is not authenticated",
                                  Toast.LENGTH_SHORT
                              ).show()
                          }
                      }
                    Webviewbutton.setOnClickListener {
                        val bundle = Bundle().apply {
                            putString("url", News.Newsurl)
                        }
                        val fragment = Webview().apply {
                            arguments = bundle
                        }
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.flFragment, fragment)
                            addToBackStack(null)
                            commit()

                        }

                    }
                }
            }
        }
    }

        fun deleteBookmark() {
            val userEmail = FirebaseAuth.getInstance().currentUser?.email
            if (userEmail != null) {
                db.collection("User").document(userEmail)
                    .collection("data").get().addOnSuccessListener { docref ->
                        val position = viewModel.clickedBookmark?.position ?: 0
                        if (position in 0 until docref.documents.size) {
                            db.collection("User").document(userEmail)
                                .collection("data").document(viewModel.clickedBookmark?.title.toString())
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Document successfully deleted!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                   val fragment =Bookmark()
                                    parentFragmentManager.beginTransaction().apply {
                                        replace(R.id.flFragment,fragment)
                                        addToBackStack(null)
                                        commit()
                                    }

                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        context,
                                        "Error deleting document: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.w("Firestore", "Error deleting document", e)
                                }
                        } else {
                            Toast.makeText(
                                context,
                                "Invalid position",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Error getting documents: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.w("Firestore", "Error getting documents", e)
                    }
            } else {
                Toast.makeText(
                    context,
                    "User is not authenticated",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }





      fun checkBookmark(newsdetailtitle: String) {
            val snewsdetailtitle = newsdetailtitle
            val userEmail = FirebaseAuth.getInstance().currentUser?.email

            CoroutineScope(Dispatchers.Main).launch {
                val refresh = userEmail?.let { email ->
                    db.collection("User").document(email)
                        .collection("data").document(snewsdetailtitle).get().await()
                }

                val string = refresh?.getString("title") ?: "Unknown Title"

                if (string != snewsdetailtitle) {
                    binding?.like?.setImageResource(R.drawable.whiteheart)
                } else {
                    binding?.like?.setImageResource(R.drawable.redheart)
                }
            }
        }


    }