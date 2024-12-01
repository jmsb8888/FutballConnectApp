package com.task.futballconnectapp.data.bd.models

data class Post(
    val idPost: Int? = null,
    val userName: String,
    val userProfileImageUrl: String,
    val mainImageUrl: String,
    val title: String,
    val description: String,
    val matchResult: MatchResult? = null,
    val person: PostPerson? = null,
    val isLiked: Boolean?,
    val comments: List<Comment> = listOf()
)
