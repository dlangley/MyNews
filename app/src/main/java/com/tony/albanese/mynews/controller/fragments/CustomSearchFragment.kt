package com.tony.albanese.mynews.controller.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tony.albanese.mynews.R
import com.tony.albanese.mynews.controller.adapters.ArticleRecyclerAdapter
import com.tony.albanese.mynews.controller.utilities.connectToSite
import com.tony.albanese.mynews.controller.utilities.generateArticleArray
import com.tony.albanese.mynews.controller.utilities.readDataFromConnection
import com.tony.albanese.mynews.controller.utilities.stringToUrl
import com.tony.albanese.mynews.model.Article
import kotlinx.android.synthetic.main.fragment_base_layout.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

//This fragment displays the results of the user's custom search.

class CustomSearchFragment : Fragment() {
    var list = ArrayList<Article>()
    lateinit var customSearchUrl: String
    lateinit var articleAdapter: ArticleRecyclerAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val subjectTextView = text_view_subject
        subjectTextView.text = getString(R.string.custom_search_title)


        customSearchUrl = "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=8768b5f889974203a05b462b8b7dc800&q=trump&fq=Business%20Politics&begin_date=20100909&end_date=20180909"
        recyclerView = fragment_recycler_view
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        fetchArticles()

    }

    fun fetchArticles() {
        val connection = connectToSite(stringToUrl(customSearchUrl)!!)
        //swipeLayout.isRefreshing = true
        doAsync {
            val result = readDataFromConnection(connection!!)
            uiThread {
               // tempList = generateArticleArray(2, result)
                list = generateArticleArray(3, result)
                articleAdapter = ArticleRecyclerAdapter(list, context!!, { view: View, article: Article -> onArticleClicked(view, article) })
                //swipeLayout.isRefreshing = false
                recyclerView.adapter = articleAdapter
            }
        }
    }

    fun onArticleClicked(view: View, article: Article) {
        view.setBackgroundColor(resources.getColor(R.color.colorAccent))
        article.mIsRead = true
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.mUrl))
        startActivity(intent)
    }
}
