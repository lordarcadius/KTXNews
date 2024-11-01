package com.vipuljha.ktxnews.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vipuljha.ktxnews.NewsApplication
import com.vipuljha.ktxnews.data.repository.NewsRepository
import com.vipuljha.ktxnews.models.Article
import com.vipuljha.ktxnews.models.NewsResponse
import com.vipuljha.ktxnews.utils.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(
    app: Application,
    val repository: NewsRepository
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countrycode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countrycode)
    }

    private suspend fun safeBreakingNewsCall(countrycode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternet()) {
                val response = repository.getBreakingNew(countrycode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeSearchNewsCall(keyword: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternet()) {
                val response = repository.searchNews(keyword, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun searchNews(keyword: String) = viewModelScope.launch {
        safeSearchNewsCall(keyword)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse!!.articles
                    val newArticles = resultResponse.articles
                    oldArticles.addAll(newArticles)
                    breakingNewsResponse = breakingNewsResponse?.copy(articles = oldArticles)
                }
                breakingNewsPage++
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse!!.articles
                    val newArticles = resultResponse.articles
                    oldArticles.addAll(newArticles)
                    searchNewsResponse = searchNewsResponse?.copy(articles = oldArticles)
                }
                searchNewsPage++
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }


        return Resource.Error(response.message())
    }

    fun getSavedNews() = repository.getAllArticles()

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.update(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    private fun hasInternet(): Boolean {
        val connectivityManager =
            getApplication<NewsApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
            return false
        }
    }
}