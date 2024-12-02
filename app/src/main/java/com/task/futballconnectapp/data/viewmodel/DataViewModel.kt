package com.task.futballconnectapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.futballconnectapp.UiState.bd.CommentUiState
import com.task.futballconnectapp.UiState.bd.PostMatchUiState
import com.task.futballconnectapp.UiState.bd.PostPlayerUiState
import com.task.futballconnectapp.UiState.bd.PostUiState
import com.task.futballconnectapp.UiState.bd.UserUiState
import com.task.futballconnectapp.UiState.bd.UsersUiState
import com.task.futballconnectapp.data.bd.models.Comment
import com.task.futballconnectapp.data.bd.models.MatchResult
import com.task.futballconnectapp.data.bd.models.Post
import com.task.futballconnectapp.data.bd.models.PostPerson
import com.task.futballconnectapp.data.bd.models.User
import com.task.futballconnectapp.data.bd.remote.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val userDao: UserDao,
) : ViewModel() {

    private val _usersAll = MutableStateFlow(UsersUiState(isLoading = true))
    val userAll: StateFlow<UsersUiState> = _usersAll.asStateFlow()

    private val _user = MutableStateFlow(UserUiState(isLoading = true))
    val user: StateFlow<UserUiState> = _user.asStateFlow()

    private val _postMatch = MutableStateFlow(PostMatchUiState(isLoading = true))
    val postMatch: StateFlow<PostMatchUiState> = _postMatch.asStateFlow()

    private val _postPlayers = MutableStateFlow(PostPlayerUiState(isLoading = true))
    val postPlayers: StateFlow<PostPlayerUiState> = _postPlayers.asStateFlow()


    private val _post = MutableStateFlow(PostUiState(isLoading = true))
    val post: StateFlow<PostUiState> = _post.asStateFlow()

    private val _comments = MutableStateFlow(CommentUiState(isLoading = true))
    val comments: StateFlow<CommentUiState> = _comments.asStateFlow()


    fun fetchUsers() {
        viewModelScope.launch {
            _usersAll.value = UsersUiState(isLoading = true)
            try {
                val users = userDao.getUsers()
                _usersAll.value = UsersUiState(users = users, isLoading = false)
            } catch (e: Exception) {
                _usersAll.value = UsersUiState(isLoading = false)
            }
        }
    }

    fun fetchUserByEmail(email: String) {
        viewModelScope.launch {
            try {
                val user = userDao.getUserByEmail(email)
                if (user != null) {
                    _user.value = UserUiState(user = user, isLoading = false)
                }
            } catch (e: Exception) {
                _user.value = UserUiState(isLoading = false)
            }
        }
    }

    fun fetchAllPostPlayer(idUser: Int) {
        viewModelScope.launch {
            _postPlayers.value = PostPlayerUiState(isLoading = true)
            try {
                val post = userDao.getAllPosts(-1, idUser)
                _postPlayers.value = PostPlayerUiState(postPlayers = post, isLoading = false)
            } catch (e: Exception) {
                _postPlayers.value = PostPlayerUiState(isLoading = false)
            }
        }
    }

    fun fetchAllPostMatches(idUser: Int) {
        viewModelScope.launch {
            _postMatch.value = PostMatchUiState(isLoading = true)
            try {
                val post = userDao.getAllPosts(1, idUser)
                _postMatch.value = PostMatchUiState(postMatches = post, isLoading = false)
            } catch (e: Exception) {
                _postMatch.value = PostMatchUiState(isLoading = false)
            }
        }
    }

    fun fetchComments(idPost: Int) {
        viewModelScope.launch {
            _comments.value = CommentUiState(isLoading = true)
            try {
                val post = userDao.getCommentsForPost(idPost)
                _comments.value = CommentUiState(commentList = post, isLoading = false)
            } catch (e: Exception) {
                _comments.value = CommentUiState(isLoading = false)
            }
        }
    }


    suspend fun createPost(post: Post): Long? {
        return withContext(Dispatchers.IO) {
            try {
                val post = userDao.createPost(post)
                return@withContext post
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }

    suspend fun createPostMatch(matchResult: MatchResult): MatchResult? {
        return withContext(Dispatchers.IO) {
            try {
                val post = userDao.createMatchResult(matchResult)
                return@withContext post
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }

    suspend fun createPostPlayer(person: PostPerson): PostPerson? {
        return withContext(Dispatchers.IO) {
            try {
                val post = userDao.createPostPerson(person)
                return@withContext post
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }

    suspend fun createUser(user: User): User? {
        return withContext(Dispatchers.IO) {
            try {
                val post = userDao.createUser(user)
                return@withContext post
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }

    suspend fun createComment(comment: Comment): Comment? {
        return withContext(Dispatchers.IO) {
            try {
                val post = userDao.createComment(comment)
                return@withContext post
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }


    suspend fun addLike(idPost: Int, idUser: Int): Long? {
        return withContext(Dispatchers.IO) {
            try {
                val post = userDao.addLike(idPost, idUser)
                return@withContext post
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }

    suspend fun removeLike(idPost: Int, idUser: Int): Boolean? {
        return withContext(Dispatchers.IO) {
            try {
                val post = userDao.removeLike(idPost, idUser)
                return@withContext post
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }
}
