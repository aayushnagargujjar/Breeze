package com.example.loginsignup

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apifetch.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Breezenews : AppCompatActivity() {
    lateinit var myRecyclerView: RecyclerView
    lateinit var newsArrayList: ArrayList<News>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_breezenews)

        // Set padding for system bars (edge-to-edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        myRecyclerView = findViewById(R.id.recyclerview)
        newsArrayList = arrayListOf()
        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.adapter = MyAdapterNews(newsArrayList, this)

        fetchNews()
    }

    private fun fetchNews() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://news-api14.p.rapidapi.com/")  // Make sure baseUrl is correct
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiInterface::class.java)
        val call = service.getNews()

        call.enqueue(object : Callback<AllNews> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<AllNews>, response: Response<AllNews>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val dataList = responseBody?.data

                    if (dataList != null) {
                        for (item in dataList) {
                            val news = News().apply {
                                title = item.title ?: "No Title"
                                ImageUrl = item.thumbnail ?: "No Image URL"
                                description = item.excerpt ?: "No Description"
                            }
                            newsArrayList.add(news)
                        }
                        myRecyclerView.adapter?.notifyDataSetChanged()
                    }
                    else {
                        Log.e("Error", "Data list is null")
                    }
                } else {
                    Log.e("Error", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AllNews>, t: Throwable) {
                Log.e("Error", "Failed to fetch news: ${t.message}")
            }
        })
    }
}
