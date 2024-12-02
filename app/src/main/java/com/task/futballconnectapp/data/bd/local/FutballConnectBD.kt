package com.task.futballconnectapp.data.bd.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.futballconnectapp.data.bd.local.dao.CommentDao
import com.task.futballconnectapp.data.bd.local.dao.MatchResultDao
import com.task.futballconnectapp.data.bd.local.dao.PostDao
import com.task.futballconnectapp.data.bd.local.dao.PostLikesDao
import com.task.futballconnectapp.data.bd.local.dao.PostPersonDao
import com.task.futballconnectapp.data.bd.local.dao.UserDao
import com.task.futballconnectapp.data.bd.local.entities.CommentEntity
import com.task.futballconnectapp.data.bd.local.entities.MatchResultEntity
import com.task.futballconnectapp.data.bd.local.entities.PostEntity
import com.task.futballconnectapp.data.bd.local.entities.PostLikesEntity
import com.task.futballconnectapp.data.bd.local.entities.PostPersonEntity
import com.task.futballconnectapp.data.bd.local.entities.UserEntity

@Database(
    entities = [
        PostEntity::class,
        CommentEntity::class,
        PostLikesEntity::class,
        MatchResultEntity::class,
        PostPersonEntity::class,
        UserEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun postLikesDao(): PostLikesDao
    abstract fun matchResultDao(): MatchResultDao
    abstract fun postPersonDao(): PostPersonDao
    abstract fun userDao(): UserDao
}
