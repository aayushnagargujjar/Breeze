package com.example.loginsignup

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Sportnews : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sportnews)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Launching a coroutine to call the API
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getTopNewsHeadlines(
                    apiKey = "122b98eeebmsh38d3a9c85f0a22fp1de28ejsncf155fd24963",
                    apiHost = "get-top-news-headlines-by-region.p.rapidapi.com"
                )
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    if (newsResponse != null) {
                        val newsText = StringBuilder()
                        for (article in newsResponse.article_list) {
                            newsText.append("Title: ${article.title}\n")
                            newsText.append("Source: ${article.source_name}\n")
                            newsText.append("Published Date: ${article.published_date}\n")
                            newsText.append("URL: ${article.url}\n")
                            newsText.append("------------------------\n")
                        }
                        withContext(Dispatchers.Main) {
                            val textView = findViewById<TextView>(R.id.textViewCategories)
                            textView.text = newsText.toString()
                        }
                    } else {
                        Log.e("Sportnews", "Empty response")
                    }
                } else {
                    Log.e("Sportnews", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Sportnews", "Exception: ${e.message}")
            }
        }
    }
}
