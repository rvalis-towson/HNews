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

// TODO - Part 3. Create the Adapter
