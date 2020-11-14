// Step 1
private val newsList = mutableListOf<NewsItem>()

override fun newsCount(): Int {
    return newsList.size
}

override fun newsItem(pos: Int): NewsItem {
    return newsList[pos]
}

// Step 2
class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.article_title
    val url: TextView = view.article_url
    val position: TextView = view.position
}

// Step 3
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

// Step 4
val adapter = NewsAdapter(this)
recyclerView.adapter = adapter
recyclerView.layoutManager = LinearLayoutManager(this)

// Step 5
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

// Part 6
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

private fun showListAndHideProgress() {
    progressBar.visibility = View.GONE
    recyclerView.visibility = View.VISIBLE
}

// Step 7
override fun showArticle(pos: Int) {
    val item = newsList[pos]
    val uri = Uri.parse(item.url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(intent)
}
