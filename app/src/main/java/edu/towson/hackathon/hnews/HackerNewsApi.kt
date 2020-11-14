package edu.towson.hackathon.hnews

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import either.Either
import okhttp3.OkHttpClient
import okhttp3.Request

private const val NUM_RESULTS = 20

interface IHackerNewsApi {
    fun fetchNews(): Either<String, List<NewsItem>>
}

class HackerNewsApi {
    private val client: OkHttpClient = OkHttpClient() // to fetch the json
    private val gson = Gson() // to deserialize the json result

    // TODO - Part 5. Fetch the news
}