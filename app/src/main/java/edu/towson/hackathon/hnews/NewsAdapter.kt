package edu.towson.hackathon.hnews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.news_item_view.view.*

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.article_title
    val url: TextView = view.article_url
    val position: TextView = view.position
}

class NewsAdapter(private val activity: MainActivity) : RecyclerView.Adapter<NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item_view, parent, false)
        val holder = NewsViewHolder(view)
        view.article_url.setOnClickListener {
            val position = holder.adapterPosition
            activity.showArticle(position)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return activity.newsCount()
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = activity.newsItem(position)
        holder.title.text = newsItem.title
        holder.url.text = newsItem.url
        holder.position.text = (position + 1).toString()
    }
}
