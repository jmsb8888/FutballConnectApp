package com.task.futballconnectapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.task.futballconnectapp.data.bd.models.Comment
import com.task.futballconnectapp.data.bd.models.MatchResult
import com.task.futballconnectapp.data.bd.models.Post
import com.task.futballconnectapp.data.bd.models.PostPerson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RoomViewModel @Inject constructor(
    private val postDao: PostDao,
    private val commentDao: CommentDao,
    private val postLikesDao: PostLikesDao,
    private val matchResultDao: MatchResultDao,
    private val postPersonDao: PostPersonDao,
    private val userDao: UserDao,
) : ViewModel() {

    fun insertPost(post: PostEntity) {
        viewModelScope.launch {
            postDao.insertPost(post)
        }
    }

    fun getAllPostsPlayer(idUser: Int, callback: (List<Post>) -> Unit) {
        viewModelScope.launch {
            val posts = postDao.getAllPosts()
            val filteredPosts = posts
                .filter { it.personId != null }
                .map { postEntity ->
                    val personEntity = postPersonDao.getPostPersonById(postEntity.personId!!)
                    val liked = postLikesDao.getPostLike(postEntity.id, idUser)
                    var isLiked: Boolean = false;
                    if (liked != null) {
                        isLiked = true;
                    }
                    Post(
                        idPost = postEntity.id,
                        userName = postEntity.userName,
                        userProfileImageUrl = postEntity.userProfileImageUrl,
                        mainImageUrl = postEntity.mainImageUrl,
                        title = postEntity.title,
                        description = postEntity.description,
                        matchResult = null,
                        person = personEntity?.let { person ->
                            PostPerson(
                                id = person.id,
                                name = person.name,
                                position = person.position,
                                dateOfBirth = person.dateOfBirth,
                                nationality = person.nationality
                            )
                        },
                        isLiked = isLiked,
                        comments = listOf()
                    )
                }
            callback(filteredPosts)
        }
    }

    fun getAllPostsMatches(idUser: Int, callback: (List<Post>) -> Unit) {
        viewModelScope.launch {
            val posts = postDao.getAllPosts()
            val filteredPosts = posts
                .filter { it.matchResultId != null }
                .map { postEntity ->
                    val matchesEntity =
                        matchResultDao.getMatchResultById(postEntity.matchResultId!!)
                    val liked = postLikesDao.getPostLike(postEntity.id, idUser)
                    var isLiked: Boolean = false;
                    if (liked != null) {
                        isLiked = true;
                    }
                    Post(
                        idPost = postEntity.id,
                        userName = postEntity.userName,
                        userProfileImageUrl = postEntity.userProfileImageUrl,
                        mainImageUrl = postEntity.mainImageUrl,
                        title = postEntity.title,
                        description = postEntity.description,
                        matchResult = matchesEntity?.let { match ->
                            MatchResult(
                                id = match.id,
                                homeTeam = match.homeTeamId,
                                awayTeam = match.awayTeamId,
                                fullTimeScoreHome = match.fullTimeScoreHome,
                                fullTimeScoreAway = match.fullTimeScoreAway,
                            )
                        },
                        person = null,
                        isLiked = isLiked,
                        comments = listOf()
                    )
                }
            callback(filteredPosts)
        }
    }


    fun deletePost(post: PostEntity) {
        viewModelScope.launch {
            postDao.deletePost(post)
        }
    }

    fun insertComment(comment: CommentEntity) {
        viewModelScope.launch {
            commentDao.insertComment(comment)
        }
    }

    fun getCommentsForPost(postId: Int, callback: (List<Comment>) -> Unit) {
        viewModelScope.launch {
            var comments = commentDao.getCommentsForPost(postId)
            if (comments.isEmpty()) {
                comments = commentDao.getCommentsForPostlocal(postId)
            }
            val commentsPost = comments.map { commentEntity ->
                Comment(
                    id = commentEntity.id,
                    postId = commentEntity.postId,
                    userName = commentEntity.userName,
                    text = commentEntity.text
                )
            }
            callback(commentsPost)
        }
    }

    fun likePost(postLikes: PostLikesEntity) {
        viewModelScope.launch {
            postLikesDao.insertPostLike(postLikes)
        }
    }

    fun unlikePost(postLikes: PostLikesEntity) {
        viewModelScope.launch {
            postLikesDao.deletePostLike(postLikes)
        }
    }

    suspend fun insertMatchResult(person: MatchResultEntity): Long? {
        val id = matchResultDao.insertMatchResult(person)
        if (id > 0) {
            return id
        }
        return null
    }

    suspend fun insertPostPerson(person: PostPersonEntity): Long? {
        val id = postPersonDao.insertPostPerson(person)
        if (id > 0) {
            return id
        }
        return null
    }

    fun insertUser(user: UserEntity) {
        viewModelScope.launch {
            userDao.insertUser(user)
        }
    }


    fun validateUser(email: String, password: String, callback: (UserEntity?) -> Unit) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)
            when {
                user == null -> callback(null)
                user.password != password -> callback(null)
                else -> callback(user)
            }
        }
    }


}