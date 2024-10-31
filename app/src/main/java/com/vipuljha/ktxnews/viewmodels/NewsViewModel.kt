package com.vipuljha.ktxnews.viewmodels

import androidx.lifecycle.ViewModel
import com.vipuljha.ktxnews.data.repository.NewsRepository

class NewsViewModel(val repository: NewsRepository) : ViewModel() {
}