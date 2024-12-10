package com.example.loginsignup

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
     val BASE_URL = "https://get-top-news-headlines-by-region.p.rapidapi.com/"

    val api: AllSportsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AllSportsApi::class.java)
    }
}
