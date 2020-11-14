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

class HackerNewsApi : IHackerNewsApi {
    private val client: OkHttpClient = OkHttpClient() // to fetch the json
    private val gson = Gson() // to deserialize the json result

    override fun fetchNews(): Either<String, List<NewsItem>> {
        try {
            val request = Request.Builder()
                .url("https://hacker-news.firebaseio.com/v0/topstories.json")
                .get()
                .build()
            val result: String? = client.newCall(request).execute().body?.string()

            val itemType = object : TypeToken<List<Int>>() {}.type
            val items = gson.fromJson<List<Int>>(result, itemType)

            val resultList = mutableListOf<NewsItem>()

            items.take(NUM_RESULTS).forEach { id ->
                val newsItem = fetchNewsItem(id)
                resultList.add(newsItem)
            }

            return Either.Right(resultList)
        } catch (e: Exception) {
            return Either.Left(e.toString())
        }
    }

    private fun fetchNewsItem(id: Int): NewsItem {
        val req = Request.Builder()
            .url("https://hacker-news.firebaseio.com/v0/item/${id}.json")
            .get()
            .build()
        val res: String? = client.newCall(req).execute().body?.string()
        val newsItem = gson.fromJson(res, NewsItem::class.java)
        return newsItem
    }
}