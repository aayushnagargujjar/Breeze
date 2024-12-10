package com.example.loginsignup

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AllNews : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_news)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://api.thenewsapi.com/v1/news/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getNews()
        retrofitData.enqueue(object : Callback<Mydatanews?> {
            override fun onResponse(call: Call<Mydatanews?>, response: Response<Mydatanews?>) {
                if (response.isSuccessful) {
                    val news = response.body()
                    val newsList = news?.data?.general
                    val collectDataInSB = StringBuilder()

                    newsList?.forEach { myData ->
                        collectDataInSB.append(myData.title).append("\n")
                    }

                    val textView = findViewById<TextView>(R.id.textViewnews)
                    textView.text = collectDataInSB.toString()
                } else {
                    Log.d("AllNews", "Response failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Mydatanews?>, t: Throwable) {
                Log.d("AllNews", "onFailure: ${t.message}")
            }
        })
    }
}
