package com.androiddevs.mvvmnewsapp.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.Source
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ArticleDaoTest {
    private lateinit var database: ArticleDatabase
    private lateinit var dao: ArticleDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ArticleDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getArticleDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertArticle() = runBlockingTest {
        val article = Article(1,"author","content","description",
            "published", Source(1,"nameSource"),"title","www.test.com",
        "image")
        //dao.insert
    }
}