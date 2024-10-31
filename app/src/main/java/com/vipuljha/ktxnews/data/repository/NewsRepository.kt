package com.vipuljha.ktxnews.data.repository

import com.vipuljha.ktxnews.data.local.ArticleDB
import com.vipuljha.ktxnews.data.remote.RetrofitProvider

class NewsRepository(val db: ArticleDB) {
    suspend fun getBreakingNew(countryCode: String, pageNumber: Int) =
        RetrofitProvider.api.getBreakingNews(countryCode, pageNumber)
}