
/*
package com.abd.news.database

import com.abd.news.model.Article
import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.abd.news.model.Source
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



@Database(entities = [com.abd.news.model.Article::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // Register the Converters here
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun dao(): ArticleDao
}


object DatabaseInstance {
    @Volatile
    private var INSTANCE: ArticleDatabase? = null

    fun getDatabase(context: Context): ArticleDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}


class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromSource(source: Source?): String? {
        return gson.toJson(source)
    }

    @TypeConverter
    fun toSource(sourceString: String?): Source? {
        val type = object : TypeToken<Source>() {}.type
        return gson.fromJson(sourceString, type)
    }
}





 */