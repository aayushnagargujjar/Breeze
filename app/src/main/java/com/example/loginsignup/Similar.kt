package com.example.loginsignup

data class Similar(
    val categories: List<String>,
    val description: String,
    val image_url: String,
    val keywords: String,
    val language: String,
    val locale: String,
    val published_at: String,
    val snippet: String,
    val source: String,
    val title: String,
    val url: String,
    val uuid: String
)