package com.example.loginsignup

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("news/headlines")
    suspend fun getNews(
        @Query("locale") locale: String = "us",
        @Query("language") language: String = "en",
        @Query("api_token") apiToken: String = "3UVK92VWvMUtEjl8EQbJrlUJxOWoJLGh3zo22qlP"
    ): Response<Mydatanews>
}
