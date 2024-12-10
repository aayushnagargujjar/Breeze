package com.example.loginsignup

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AllSportsApi {
    @GET("news/headlines")
    suspend fun getTopNewsHeadlines(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") apiHost: String
    ): Response<NewsResponse>
}
