package com.vipuljha.ktxnews.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Serializable {
    override fun hashCode(): Int {
        var result = id.hashCode()
        if (url.isNullOrBlank()) {
            result = 31 * result + url.hashCode()
        }
        return result
    }
}