package edu.towson.hackathon.hnews

/**
 * Simplified news item class
 * The actual json payload has much more data
 */
data class NewsItem(
    val title: String,
    val url: String
)
