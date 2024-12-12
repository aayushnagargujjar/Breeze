package com.example.apifetch

import com.example.loginsignup.AllNews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {
    @Headers(
        "x-rapidapi-key:122b98eeebmsh38d3a9c85f0a22fp1de28ejsncf155fd24963",
        "x-rapidapi-host: news-api14.p.rapidapi.com"
    )
    @GET("/v2/trendings")
    fun getNews(
        @Query("topic") topic: String= "Sports",
        @Query("language") language: String = "en"
    ): Call<AllNews>
}