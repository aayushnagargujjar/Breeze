package com.example.loginsignup

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.apifetch.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Breezenews1 : Fragment(R.layout.fragment_breezenews) {
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var newsArrayList: ArrayList<News>
    private lateinit var myRecyclerViewH: RecyclerView
    private lateinit var refresh: SwipeRefreshLayout
    private lateinit var topicList: ArrayList<Topic>
    private lateinit var topicRecyclerView: RecyclerView
    private lateinit var topicAdapter: TopicAdapter
    private lateinit var viewModel: BookmarkViewModel

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topicList = getSupportedTopics()
        myRecyclerView = view.findViewById(R.id.recyclerview)
        myRecyclerViewH = view.findViewById(R.id.recyclerviewhorizontal)
        refresh = view.findViewById(R.id.swipeRefreshLayout)
        topicRecyclerView = view.findViewById(R.id.topicRecyclerView)
        viewModel = ViewModelProvider(requireActivity()).get(BookmarkViewModel::class.java)

        newsArrayList = arrayListOf()

        Log.d("Breezenews", "topicList size: ${topicList.size}")

        topicRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        myRecyclerView.layoutManager = LinearLayoutManager(context)
        myRecyclerViewH.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val myAdapterNews = MyAdapterNews(newsArrayList, requireContext())
        val myAdapterNewsH = MyAdapterNewsH(newsArrayList, requireContext())
        topicAdapter = TopicAdapter(topicList, requireContext())

        myRecyclerView.adapter = myAdapterNews
        myRecyclerViewH.adapter = myAdapterNewsH
        topicRecyclerView.adapter = topicAdapter

        topicAdapter.setItemClickListener(object : TopicAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val fragment = Breezenews().apply {
                    arguments = Bundle().apply {
                        putString("topic", topicList[position].name)
                    }
                }
                setCurrentFragment(fragment)
            }
        })

        myAdapterNews.setItemClickListener(object : MyAdapterNews.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Bundle().putString("News_bookmark", "News")
                val clickedBookmark = newsArrayList[position]
                viewModel.Breezenewsclicked = clickedBookmark

                val fragment = NewsDetailFragment()
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, fragment)
                    addToBackStack(null)
                    commit()
                }
            }
        })

        myAdapterNewsH.setItemClickListener(object : MyAdapterNewsH.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Bundle().putString("News_bookmark", "News")
                handleBookmarkClick(position)
            }
        })

        fetchNews()

        refresh.setOnRefreshListener {
            fetchNews()
        }
    }

    private fun fetchNews() {
        refresh.isRefreshing = true
        val retrofit = Retrofit.Builder()
            .baseUrl("https://news-api14.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiInterface::class.java)
        val topic = arguments?.getString("topic") ?: "General"
        val call = service.getNews(topic, "en")

        call.enqueue(object : Callback<AllNews> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<AllNews>, response: Response<AllNews>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val dataList = responseBody?.data

                    if (dataList != null) {
                        newsArrayList.clear()
                        dataList.map { item ->
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

    private fun handleBookmarkClick(position: Int) {
        val clickedBookmark = newsArrayList[position]
        viewModel.Breezenewsclicked = clickedBookmark

        val fragment = NewsDetailFragment()
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}
