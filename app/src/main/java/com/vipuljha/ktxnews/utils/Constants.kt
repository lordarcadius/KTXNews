package com.vipuljha.ktxnews.utils

import com.vipuljha.ktxnews.BuildConfig

object Constants {
    val API_KEY = Helper.decodeFromBase64(BuildConfig.apiKey)
}