package com.vipuljha.ktxnews.data.repository

import com.vipuljha.ktxnews.data.local.ArticleDB
import com.vipuljha.ktxnews.data.remote.RetrofitProvider
import com.vipuljha.ktxnews.models.Article

class NewsRepository(val db: ArticleDB) {
    suspend fun getBreakingNew(countryCode: String, pageNumber: Int) =
        RetrofitProvider.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(keyword: String, pageNumber: Int) =
        RetrofitProvider.api.searchForNews(keyword, pageNumber)

    suspend fun update(article: Article) = db.getArticleDao().update(article)

    fun getAllArticles() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)


}