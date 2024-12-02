package com.task.futballconnectapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.task.futballconnectapp.data.bd.DataSynchronizer
import com.task.futballconnectapp.data.bd.local.dao.CommentDao
import com.task.futballconnectapp.data.bd.local.dao.MatchResultDao
import com.task.futballconnectapp.data.bd.local.dao.PostDao
import com.task.futballconnectapp.data.bd.local.dao.PostLikesDao
import com.task.futballconnectapp.data.bd.local.dao.PostPersonDao
import com.task.futballconnectapp.data.bd.local.dao.UserDao
import com.task.futballconnectapp.data.viewmodel.DataViewModel
import com.task.futballconnectapp.data.viewmodel.RoomViewModel
import com.task.futballconnectapp.data.viewmodel.SharedPreferencesViewModel
import com.task.futballconnectapp.ui.MyApp
import com.task.futballconnectapp.ui.MyApplicationTheme
import com.task.futballconnectapp.ui.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sharedPreferencesViewModel: SharedPreferencesViewModel by viewModels()
    private val dataViewModel: DataViewModel by viewModels()
    private val roomViewModel: RoomViewModel by viewModels()
    @Inject lateinit var postDao: PostDao
    @Inject lateinit var commentDao: CommentDao
    @Inject lateinit var postLikesDao: PostLikesDao
    @Inject lateinit var matchResultDao: MatchResultDao
    @Inject lateinit var postPersonDao: PostPersonDao
    @Inject lateinit var userDao: UserDao

    private val dataSynchronizer by lazy {
        DataSynchronizer(
            postDao, commentDao, postLikesDao, matchResultDao, postPersonDao, userDao, dataViewModel
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                MyApp(sharedPreferencesViewModel, dataViewModel, roomViewModel)
            }
        }
        if (isInternetAvailable(this)) {
            lifecycleScope.launch {
                try {
                    dataSynchronizer.synchronizeData()
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error sincronizando datos: ${e.message}")
                }
            }
        } else {
            Log.w("MainActivity", "No hay conexión a Internet. La sincronización no se realizará.")
        }
    }
}


