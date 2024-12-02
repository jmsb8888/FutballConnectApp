package com.task.futballconnectapp.data.bd

import com.task.futballconnectapp.data.bd.local.dao.CommentDao
import com.task.futballconnectapp.data.bd.local.dao.MatchResultDao
import com.task.futballconnectapp.data.bd.local.dao.PostDao
import com.task.futballconnectapp.data.bd.local.dao.PostLikesDao
import com.task.futballconnectapp.data.bd.local.dao.PostPersonDao
import com.task.futballconnectapp.data.bd.models.Comment
import com.task.futballconnectapp.data.bd.models.Post
import com.task.futballconnectapp.data.bd.models.MatchResult
import com.task.futballconnectapp.data.bd.models.PostPerson
import com.task.futballconnectapp.data.bd.local.dao.UserDao
import com.task.futballconnectapp.data.bd.models.User
import com.task.futballconnectapp.data.viewmodel.DataViewModel


class DataSynchronizer(
    private val postDao: PostDao,
    private val commentDao: CommentDao,
    private val postLikesDao: PostLikesDao,
    private val matchResultDao: MatchResultDao,
    private val postPersonDao: PostPersonDao,
    private val userDao: UserDao,
    private val dataViewModel: DataViewModel
) {

    suspend fun synchronizeData() {
        val postsToUpload = postDao.getPostsToUpload()
        val matchResultsToUpload =
            matchResultDao.getMatchResultsToUpload()
        val postPersonsToUpload =
            postPersonDao.getPostPersonsToUpload()
        val commentsToUpload =
            commentDao.getCommentsToUpload()
        val postLikesToUpload =
            postLikesDao.getPostLikesToUpload()
        val usersToUpload = userDao.getUsersToUpload()
        for (post in postsToUpload) {
            val remotePost = Post(
                userName = post.userName,
                userProfileImageUrl = post.userProfileImageUrl,
                mainImageUrl = post.mainImageUrl,
                title = post.title,
                description = post.description,
                matchResult = post.matchResultId?.let {
                    MatchResult(
                        id = it,
                        homeTeam = "homeTeam",
                        awayTeam = "awayTeam",
                        fullTimeScoreHome = 1,
                        fullTimeScoreAway = 1
                    )
                },
                person = post.personId?.let {
                    PostPerson(
                        id = it,
                        name = "homeTeam",
                        position = "awayTeam",
                        dateOfBirth = "dateOfBirth",
                        nationality = "cionality"
                    )
                },
                isLiked = post.isLiked
            )
            val createdPost =
                dataViewModel.createPost(remotePost)
            createdPost?.let {
                postDao.updatePostIdBdRemote(post.id, it.toInt())
            }
        }

        for (matchResult in matchResultsToUpload) {
            val remoteMatchResult = MatchResult(
                homeTeam = matchResult.homeTeamId,
                awayTeam = matchResult.awayTeamId,
                fullTimeScoreHome = matchResult.fullTimeScoreHome,
                fullTimeScoreAway = matchResult.fullTimeScoreAway,
                id = matchResult.id
            )
            val createdMatchResult = dataViewModel.createPostMatch(remoteMatchResult)
            createdMatchResult?.let {
                matchResultDao.updateMatchResultIdBdRemote(matchResult.id.toLong(), it.id.toLong())
            }
        }

        for (postPerson in postPersonsToUpload) {
            val remotePostPerson = PostPerson(
                name = postPerson.name,
                position = postPerson.position,
                dateOfBirth = postPerson.dateOfBirth,
                nationality = postPerson.nationality,
                id = postPerson.id
            )
            val createdPostPerson = dataViewModel.createPostPlayer(remotePostPerson)
            createdPostPerson?.let {
                postPersonDao.updatePostPersonIdBdRemote(postPerson.id.toLong(), it.id.toLong())
            }
        }

        for (comment in commentsToUpload) {
            val remoteComment = Comment(
                postId = comment.postId,
                userName = comment.userName,
                text = comment.text,
                id = null
            )
            val createdComment = dataViewModel.createComment(remoteComment)
            createdComment?.let {
                it.id?.let { it1 ->
                    commentDao.updateCommentIdBdRemote(
                        comment.id.toLong(),
                        it1.toLong()
                    )
                }
            }
        }

        for (postLike in postLikesToUpload) {
            val postId = postLike.postId
            val userId = postLike.userId
            val createdPostLike = dataViewModel.addLike(postId, userId)
            createdPostLike?.let {
                postLikesDao.updatePostLikeIdBdRemote(postLike.id.toLong(), it)
            }
        }

        for (user in usersToUpload) {
            val remoteUser = User(
                name = user.name,
                email = user.email,
                imagePerfil = user.imagePerfil,
                password = user.password,
                id = user.id
            )
            val createdUser = dataViewModel.createUser(remoteUser)
            createdUser?.let {
                userDao.updateUserIdBdRemote(user.id.toLong(), it.id.toLong())
            }
        }
    }
}
