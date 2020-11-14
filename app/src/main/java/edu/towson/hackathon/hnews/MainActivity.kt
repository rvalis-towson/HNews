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
        val item = newsList[pos]
        val uri = Uri.parse(item.url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
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
        scope.launch(Dispatchers.IO) {
            when(val items = HackerNewsApi().fetchNews()) {
                is Either.Right -> {
                    newsList.clear()
                    newsList.addAll(items.value)
                    withContext(Dispatchers.Main) {
                        recyclerView.adapter?.notifyDataSetChanged()
                        showListAndHideProgress()
                    }
                }
                is Either.Left -> {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Error fetching news", Toast.LENGTH_SHORT).show()
                        showListAndHideProgress()
                    }
                }
            }
        }
    }

    private fun showListAndHideProgress() {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}