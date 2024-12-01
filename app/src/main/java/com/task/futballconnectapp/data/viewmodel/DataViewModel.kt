package com.task.futballconnectapp.data.viewmodel

import android.util.Log
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
    private val userDao: UserDao
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


    // Obtener usuarios desde la base de datos
    fun fetchUsers() {
        viewModelScope.launch {
            _usersAll.value = UsersUiState(isLoading = true) // Comienza a cargar
            try {
                Log.d("DataViewModel", "fetchUsers: ")
                val users = userDao.getUsers() // Recupera usuarios desde la base de datos
                Log.d("DataViewModel", "fetchUsers: $users")
                _usersAll.value = UsersUiState(users = users, isLoading = false) // Termina la carga
            } catch (e: Exception) {
                _usersAll.value = UsersUiState(isLoading = false)
                Log.e("DataViewModel", "Error fetching users", e)
            }
        }
    }

    fun fetchUserByEmail(email: String) {
        viewModelScope.launch {
            try {
                val user = userDao.getUserByEmail(email)
                if (user != null) {
                    Log.d("DataViewModel", "User found: $user")
                    _user.value = UserUiState(user = user, isLoading = false)
                } else {
                    Log.d("DataViewModel", "No user found with email: $email")
                }
            } catch (e: Exception) {
                _user.value = UserUiState(isLoading = false)
                Log.e("DataViewModel", "Error fetching user by email", e)
            }
        }
    }

    fun fetchAllPostPlayer(idUser: Int) {
        viewModelScope.launch {
            _postPlayers.value = PostPlayerUiState(isLoading = true) // Comienza a cargar
            try {
                Log.d("DataViewModel", "fetchUsers: ")
                val post =
                    userDao.getAllPosts(-1, idUser) // Recupera usuarios desde la base de datos
                Log.d("DataViewModel", "fetchUsers: $post")
                _postPlayers.value =
                    PostPlayerUiState(postPlayers = post, isLoading = false) // Termina la carga
            } catch (e: Exception) {
                _postPlayers.value = PostPlayerUiState(isLoading = false)
                Log.e("DataViewModel", "Error fetching users", e)
            }
        }
    }

    fun fetchAllPostMatches(idUser: Int) {
        viewModelScope.launch {
            _postMatch.value = PostMatchUiState(isLoading = true) // Comienza a cargar
            try {
                Log.d("DataViewModel", "fetchpostMatches: ")
                val post =
                    userDao.getAllPosts(1, idUser) // Recupera usuarios desde la base de datos
                Log.d("DataViewModel postMatches", "fetchpostMatches: $post")
                _postMatch.value =
                    PostMatchUiState(postMatches = post, isLoading = false) // Termina la carga
            } catch (e: Exception) {
                _postMatch.value = PostMatchUiState(isLoading = false)
                Log.e("DataViewModel postMatches", "Error fetching postMatches", e)
            }
        }
    }

    fun fetchComments(idPost: Int) {
        viewModelScope.launch {
            _comments.value = CommentUiState(isLoading = true) // Comienza a cargar
            try {
                val post =
                    userDao.getCommentsForPost(idPost) // Recupera usuarios desde la base de datos
                Log.d("comenarios", "obtenido: $post  idenviado: $idPost")
                _comments.value =
                    CommentUiState(commentList = post, isLoading = false) // Termina la carga
            } catch (e: Exception) {
                _comments.value = CommentUiState(isLoading = false)
                Log.e("DataViewModel postMatches", "Error fetching postMatches", e)
            }
        }
    }


    suspend fun createPost(post: Post): Boolean? {
        // Usamos conContext para hacer la operación en el hilo IO
        return withContext(Dispatchers.IO) {
            try {
                Log.d("DataViewModel", "Creating post match result")
                // Llamamos a la función de creación de MatchResult en la base de datos
                val post = userDao.createPost(post)
                Log.d("DataViewModel postMatches", "Match result created: $post")
                return@withContext post // Devolvemos el objeto creado
            } catch (e: Exception) {
                Log.e("DataViewModel postMatches", "Error creating post match", e)
                return@withContext null // Si ocurre un error, devolvemos null
            }
        }
    }

    suspend fun createPostMatch(matchResult: MatchResult): MatchResult? {
        // Usamos conContext para hacer la operación en el hilo IO
        return withContext(Dispatchers.IO) {
            try {
                Log.d("DataViewModel", "Creating post match result")
                // Llamamos a la función de creación de MatchResult en la base de datos
                val post = userDao.createMatchResult(matchResult)
                Log.d("DataViewModel postMatches", "Match result created: $post")
                return@withContext post // Devolvemos el objeto creado
            } catch (e: Exception) {
                Log.e("DataViewModel postMatches", "Error creating post match", e)
                return@withContext null // Si ocurre un error, devolvemos null
            }
        }
    }

    suspend fun createPostPlayer(person: PostPerson): PostPerson? {
        // Usamos conContext para hacer la operación en el hilo IO
        return withContext(Dispatchers.IO) {
            try {
                Log.d("DataViewModel", "Creating post person result")
                // Llamamos a la función de creación de MatchResult en la base de datos
                val post = userDao.createPostPerson(person)
                Log.d("DataViewModel postPerson", "Match result created: $post")
                return@withContext post // Devolvemos el objeto creado
            } catch (e: Exception) {
                Log.e("DataViewModel postPerson", "Error creating post match", e)
                return@withContext null // Si ocurre un error, devolvemos null
            }
        }
    }

    suspend fun createUser(user: User): User? {
        // Usamos conContext para hacer la operación en el hilo IO
        return withContext(Dispatchers.IO) {
            try {
                Log.d("DataViewModel", "Creating post person result")
                // Llamamos a la función de creación de MatchResult en la base de datos
                val post = userDao.createUser(user)
                Log.d("DataViewModel postPerson", "Match result created: $post")
                return@withContext post // Devolvemos el objeto creado
            } catch (e: Exception) {
                Log.e("DataViewModel postPerson", "Error creating post match", e)
                return@withContext null // Si ocurre un error, devolvemos null
            }
        }
    }

    suspend fun createComment(comment: Comment): Comment? {
        // Usamos conContext para hacer la operación en el hilo IO
        return withContext(Dispatchers.IO) {
            try {
                Log.d("DataViewModel", "Creating post person result")
                // Llamamos a la función de creación de MatchResult en la base de datos
                val post = userDao.createComment(comment)
                Log.d("DataViewModel postPerson", "Match result created: $post")
                return@withContext post // Devolvemos el objeto creado
            } catch (e: Exception) {
                Log.e("DataViewModel postPerson", "Error creating post match", e)
                return@withContext null // Si ocurre un error, devolvemos null
            }
        }
    }


    suspend fun addLike(idPost: Int, idUser: Int): Boolean? {
        // Usamos conContext para hacer la operación en el hilo IO
        return withContext(Dispatchers.IO) {
            try {
                Log.d("DataViewModel", "Creating post person result")
                // Llamamos a la función de creación de MatchResult en la base de datos
                val post = userDao.addLike(idPost, idUser)
                Log.d("DataViewModel postPerson", "Match result created: $post")
                return@withContext post // Devolvemos el objeto creado
            } catch (e: Exception) {
                Log.e("DataViewModel postPerson", "Error creating post match", e)
                return@withContext null // Si ocurre un error, devolvemos null
            }
        }
    }

    suspend fun removeLike(idPost: Int, idUser: Int): Boolean? {
        // Usamos conContext para hacer la operación en el hilo IO
        return withContext(Dispatchers.IO) {
            try {
                Log.d("DataViewModel", "Creating post person result")
                // Llamamos a la función de creación de MatchResult en la base de datos
                val post = userDao.removeLike(idPost, idUser)
                Log.d("DataViewModel postPerson", "Match result created: $post")
                return@withContext post // Devolvemos el objeto creado
            } catch (e: Exception) {
                Log.e("DataViewModel postPerson", "Error creating post match", e)
                return@withContext null // Si ocurre un error, devolvemos null
            }
        }
    }


}
