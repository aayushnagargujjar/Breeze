 package com.example.apifetch

import com.example.loginsignup.AllNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {
    @Headers(
        "x-rapidapi-key:f86e401e5dmsh5d90ef463a23dc5p137a1fjsncfe4c4966122",
        "x-rapidapi-host: news-api14.p.rapidapi.com"
    )
    @GET("/v2/trendings")
    fun getNews(
        @Query("topic") topic: String,
        @Query("language") language: String

    ): Call<AllNews>
}