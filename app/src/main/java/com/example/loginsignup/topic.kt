package com.example.loginsignup

data class Topic(val id: String, val name: String,var isSelected: Boolean = false)

fun getSupportedTopics(): ArrayList<Topic> {
    return ArrayList(listOf(
        Topic("business", "Business"),
        Topic("entertainment", "Entertainment"),
        Topic("general", "General"),
        Topic("health", "Health"),
        Topic("lifestyle", "Lifestyle"),
        Topic("politics", "Politics"),
        Topic("science", "Science"),
        Topic("sports", "Sports"),
        Topic("technology", "Technology"),
        Topic("world", "World")
    ))
}
