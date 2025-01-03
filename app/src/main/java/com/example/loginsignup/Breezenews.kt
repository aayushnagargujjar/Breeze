package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.apifetch.ApiInterface
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Breezenews : AppCompatActivity() {
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var newsArrayList: ArrayList<News>
    private lateinit var myRecyclerViewH: RecyclerView
    private lateinit var refresh: SwipeRefreshLayout
    private lateinit var searchView: SearchView
    private lateinit var topicList: ArrayList<Topic>
    private lateinit var topicRecyclerView: RecyclerView
    private lateinit var topicAdapter: TopicAdapter
    private lateinit var switchbookmark: Switch
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breezenews)
        val profilebtn = findViewById<ImageButton>(R.id.Profile)
        refresh = findViewById(R.id.swipeRefreshLayout)
        myRecyclerView = findViewById(R.id.recyclerview)
        myRecyclerViewH = findViewById(R.id.recyclerviewhorizontal)
        searchView = findViewById(R.id.searchView)
        topicRecyclerView = findViewById(R.id.topicRecyclerView)
        switchbookmark = findViewById(R.id.switch1)

        newsArrayList = arrayListOf()
        topicList = getSupportedTopics() // Initialize topicList as an ArrayList

        topicRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerViewH.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val myAdapterNews = MyAdapterNews(newsArrayList, this)
        val myAdapterNewsH = MyAdapterNewsH(newsArrayList, this)
        topicAdapter = TopicAdapter(topicList, this)

        myRecyclerView.adapter = myAdapterNews
        myRecyclerViewH.adapter = myAdapterNewsH
        topicRecyclerView.adapter = topicAdapter

        // Initially set the RecyclerView for topics to GONE
        topicRecyclerView.visibility = View.GONE

        topicAdapter.setItemClickListener(object : TopicAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@Breezenews, Breezenews::class.java).apply {
                    putExtra("topic", topicList[position].name)
                }
                startActivity(intent)
            }
        })
        profilebtn.setOnClickListener {
           val  auth =FirebaseAuth.getInstance()
            val email =auth.currentUser?.email
            val intent = Intent(this@Breezenews, WelcomeActivity::class.java)
                .putExtra("name", intent.getStringExtra("name"))
                .putExtra("mail", email)
                .putExtra("pass", intent.getStringExtra("pass"))
            startActivity(intent)
        }
        switchbookmark.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
              val intent = Intent(this@Breezenews, Bookmark::class.java)
                startActivity(intent)
            } else {

            }}

        myAdapterNews.setItemClickListener(object : MyAdapterNews.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@Breezenews, Newsdetail::class.java).apply {
                    putExtra("title", newsArrayList[position].title)
                    putExtra("description", newsArrayList[position].description)
                    putExtra("image", newsArrayList[position].ImageUrl)
                    putExtra("url", newsArrayList[position].Newsurl)
                    putExtra("date", newsArrayList[position].date)
                    putExtra("author", newsArrayList[position].author)
                }
                startActivity(intent)
            }
        })

        myAdapterNewsH.setItemClickListener(object : MyAdapterNewsH.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@Breezenews, Newsdetail::class.java).apply {
                    putExtra("title", newsArrayList[position].title)
                    putExtra("description", newsArrayList[position].description)
                    putExtra("image", newsArrayList[position].ImageUrl)
                    putExtra("url", newsArrayList[position].Newsurl)
                    putExtra("date", newsArrayList[position].date)
                    putExtra("author", newsArrayList[position].author)
                }
                startActivity(intent)
            }
        })

        searchView.setOnSearchClickListener {
            topicRecyclerView.visibility = View.VISIBLE
        }

        searchView.setOnCloseListener {
            topicRecyclerView.visibility = View.GONE
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Do nothing
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = filter(topicList, newText)
                topicAdapter.updateList(filteredList)
                return true
            }
        })

        fetchNews()
        refresh.setOnRefreshListener {
            fetchNews()
        }
    }

    private fun filter(list: ArrayList<Topic>, query: String?): ArrayList<Topic> {
        val filteredList = arrayListOf<Topic>()
        if (query != null) {
            for (item in list) {
                if (item.name.contains(query, true) || item.subtopics.any { it.name.contains(query, true) }) {
                    filteredList.add(item)
                }
            }
        }
        return filteredList
    }

    private fun fetchNews() {
        refresh.isRefreshing = true
        val retrofit = Retrofit.Builder()
            .baseUrl("https://news-api14.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiInterface::class.java)
        val topic = intent.getStringExtra("topic") ?: "General"
        val call = service.getNews(topic, "en")

        call.enqueue(object : Callback<AllNews> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<AllNews>, response: Response<AllNews>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val dataList = responseBody?.data

                    if (dataList != null) {
                        newsArrayList.clear()
                        for (item in dataList) {
                            val news = News().apply {
                                title = item.title
                                ImageUrl = item.thumbnail
                                description = item.excerpt
                                Newsurl = item.url
                                author = item.authors.toString()
                                date = item.date.substring(0, 10) ?: "No Date"
                            }
                            newsArrayList.add(news)
                        }
                        myRecyclerView.adapter?.notifyDataSetChanged()
                        myRecyclerViewH.adapter?.notifyDataSetChanged()

                    } else {
                        Log.e("Error", "Data list is null")
                    }
                } else {
                    Log.e("Error", "Response not successful: ${response.code()}")
                }
                refresh.isRefreshing = false
            }

            override fun onFailure(call: Call<AllNews>, t: Throwable) {
                Log.e("Error", "Failed to fetch news: ${t.message}")
                refresh.isRefreshing = false
            }
        })
        Handler().postDelayed({
            refresh.isRefreshing = false
        }, 2000)
    }
}
