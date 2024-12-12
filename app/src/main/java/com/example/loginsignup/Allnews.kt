package com.example.loginsignup

data class AllNews(
    val data: List<Data>,        // List of articles (news items)
    val hitsPerPage: Int,
    val page: Int,
    val size: Int,
    val success: Boolean,
    val timeMs: Int,
    val totalHits: Int,
    val totalPages: Int
)