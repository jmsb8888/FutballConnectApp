package com.task.futballconnectapp.data.bd.local

import android.content.Context
import androidx.room.Room
import com.task.futballconnectapp.data.bd.local.dao.CommentDao
import com.task.futballconnectapp.data.bd.local.dao.MatchResultDao
import com.task.futballconnectapp.data.bd.local.dao.PostDao
import com.task.futballconnectapp.data.bd.local.dao.PostLikesDao
import com.task.futballconnectapp.data.bd.local.dao.PostPersonDao
import com.task.futballconnectapp.data.bd.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseLocalModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun providePostDao(db: AppDatabase): PostDao = db.postDao()

    @Provides
    fun provideCommentDao(db: AppDatabase): CommentDao = db.commentDao()

    @Provides
    fun providePostLikesDao(db: AppDatabase): PostLikesDao = db.postLikesDao()

    @Provides
    fun provideMatchResultDao(db: AppDatabase): MatchResultDao = db.matchResultDao()

    @Provides
    fun providePostPersonDao(db: AppDatabase): PostPersonDao = db.postPersonDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
}

