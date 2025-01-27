
/*
package com.abd.news.database

import com.abd.news.model.Article
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Upsert
    suspend fun upsertArticle(dataModel: com.abd.news.model.Article)

    @Query("SELECT * FROM articles")
    fun getAllRecords(): Flow<List<com.abd.news.model.Article>>
}

 */