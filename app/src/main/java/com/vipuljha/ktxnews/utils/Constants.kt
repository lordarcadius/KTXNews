package com.vipuljha.ktxnews.utils

import com.vipuljha.ktxnews.BuildConfig

object Constants {
    val API_KEY = Helper.decodeFromBase64(BuildConfig.apiKey)
    const val BASE_URL = "https://newsapi.org"
}