package com.abd.news.viewmodel

import com.abd.news.model.Article
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.abd.news.model.ArticleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val newsApi = RetrofitInstance.api
    val articles = mutableStateListOf<Article>()
    val isLoading = mutableStateOf(false)
    private val imageCache = mutableMapOf<String, Bitmap?>()
    fun getNews(onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = newsApi.getTopHeadlines("us")
                handleResponse(response, onError)
            } catch (e: Exception) {
                onError("Error fetching news: ${e.message}")
                println("Error fetching news: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    fun getQueries(query: String, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = newsApi.getQueries(query)
                handleResponse(response, onError)
            } catch (e: Exception) {
                onError("Error fetching news for query: ${e.message}")
                println("Error fetching news for query: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    fun getNewsByDateRange(query: String, fromDate: String, toDate: String, sortBy: String, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = newsApi.getNewsByDateRange(query, fromDate, toDate, sortBy)
                handleResponse(response, onError)
            } catch (e: Exception) {
                onError("Error fetching news by date range: ${e.message}")
                println("Error fetching news by date range: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    suspend fun getImage(url: String?): Bitmap? {
        if (url.isNullOrEmpty()) return null

        if (imageCache.containsKey(url)) {
            return imageCache[url]
        }

        val bitmap = loadImageFromUrl(url)
        imageCache[url] = bitmap
        return bitmap
    }




    fun saveArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // dao.upsertArticle(article)
            } catch (e: Exception) {
                println("Error saving article: ${e.message}")
            }


        }
    }

    private suspend fun loadImageFromUrl(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun <T> handleResponse(response: retrofit2.Response<T>, onError: (String) -> Unit) {
        if (response.isSuccessful) {
            val news = response.body() as? ArticleModel
            if (news != null) {
                articles.clear()
                articles.addAll(news.articles)
            }
        } else {
            val errorBodyString = response.errorBody()?.string()
            val errorMessage = "Error: ${response.message()} (Code: ${response.code()})\nDetails: $errorBodyString"
            onError(errorMessage)
            println(errorMessage)
        }
    }
}
