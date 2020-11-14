package edu.towson.hackathon.hnews

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import either.Either
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

/**
 * Our "Controller" interface
 */
interface IHNews {
    fun newsCount(): Int
    fun newsItem(pos: Int): NewsItem
    fun showArticle(pos: Int)
}

class MainActivity : AppCompatActivity(), IHNews {

    // By default, run coroutines on the main thread
    private val scope = CoroutineScope(Dispatchers.Main)

    private val newsList = mutableListOf<NewsItem>()

    override fun newsCount(): Int {
        return newsList.size
    }

    override fun newsItem(pos: Int): NewsItem {
        return newsList[pos]
    }

    override fun showArticle(pos: Int) {
        // TODO - Part 7. View the article
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = NewsAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchNews()
    }

    private fun fetchNews() {
        // TODO - Part 6. Call the api
    }
}