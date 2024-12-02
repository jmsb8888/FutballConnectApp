package com.task.futballconnectapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.task.futballconnectapp.data.bd.local.entities.CommentEntity
import com.task.futballconnectapp.data.bd.local.entities.PostLikesEntity
import com.task.futballconnectapp.data.bd.models.Comment
import com.task.futballconnectapp.data.bd.models.MatchResult
import com.task.futballconnectapp.data.bd.models.Post
import com.task.futballconnectapp.data.bd.models.PostPerson
import com.task.futballconnectapp.data.viewmodel.DataViewModel
import com.task.futballconnectapp.data.viewmodel.RoomViewModel
import com.task.futballconnectapp.data.viewmodel.SharedPreferencesViewModel
import kotlinx.coroutines.launch


@Composable
fun FootballPostsScreen(
    navController: NavController,
    screen: String,
    posts: List<Post>,
    dataViewModel: DataViewModel,
    sharedPreferencesViewModel: SharedPreferencesViewModel,
    roomViewModel: RoomViewModel
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (screen == "createPost") {
                        navController.navigate("createPost")
                    } else if (screen == "createPostPlayer") {
                        navController.navigate("createPostPlayer")
                    }
                },
                containerColor = Color.Transparent,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar publicación",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(posts) { post ->
                PostCard(post, dataViewModel, sharedPreferencesViewModel, roomViewModel)
            }
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    dataViewModel: DataViewModel,
    sharedPreferencesViewModel: SharedPreferencesViewModel,
    roomViewModel: RoomViewModel
) {
    val isLikedState = remember { mutableStateOf(post.isLiked) }
    val showComments = remember { mutableStateOf(false) }
    val comments = remember { mutableStateListOf<Comment>() }
    val commentUiState = dataViewModel.comments.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val contex = LocalContext.current
    LaunchedEffect(post.idPost, showComments.value) {
        if (showComments.value) {
            if (isInternetAvailable(contex)) {
                post.idPost?.let { dataViewModel.fetchComments(it) }
            } else {
                roomViewModel.getCommentsForPost(post.idPost!!) {
                    commentUiState.commentList = it
                }
            }
        }
    }
    LaunchedEffect(commentUiState) {
        if (showComments.value && commentUiState.commentList.isNotEmpty()) {
            comments.clear()
            comments.addAll(commentUiState.commentList)
        }
    }

    Box(
        modifier = Modifier.clickable { /*  */ }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(post.userProfileImageUrl),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = post.userName, style = MaterialTheme.typography.bodyLarge)
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xF0000000).copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            fontSize = 18.sp,
                            text = post.description,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Justify
                        )
                    }

                    if (post.matchResult != null) {
                        MatchResultCard(matchResult = post.matchResult)
                    } else if (post.person != null) {
                        SelectedPersonCard(person = post.person)
                    }
                }

                val isLiked = isLikedState.value ?: false
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    InteractionIcon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Me gusta",
                        isActive = isLiked,
                        activeColor = Color(0xFF4CAF50),
                        onClick = {
                            isLikedState.value = !(isLikedState.value ?: false)
                            coroutineScope.launch {
                                if (isLikedState.value == true) {
                                    val idResult = dataViewModel.addLike(
                                        post.idPost!!, sharedPreferencesViewModel.getIdUser()
                                    )
                                    roomViewModel.likePost(
                                        PostLikesEntity(
                                            postId = post.idPost,
                                            userId = sharedPreferencesViewModel.getIdUser(),
                                            idBdRemote = idResult?.toInt()
                                        )
                                    )
                                } else {
                                    dataViewModel.removeLike(
                                        post.idPost!!, sharedPreferencesViewModel.getIdUser()
                                    )
                                    roomViewModel.unlikePost(
                                        PostLikesEntity(
                                            postId = post.idPost,
                                            userId = sharedPreferencesViewModel.getIdUser()
                                        )
                                    )
                                }
                            }
                        }
                    )
                    InteractionIcon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = "Comentar",
                        isActive = false,
                        activeColor = Color.Gray,
                        onClick = { showComments.value = true }
                    )
                }
                if (showComments.value) {
                    CommentsSection(
                        comments = comments,
                        dataViewModel = dataViewModel,
                        sharedPreferencesViewModel = sharedPreferencesViewModel,
                        idPost = post.idPost,
                        roomViewModel = roomViewModel,
                        onAddComment = { newComment ->
                            comments.add(newComment)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun CommentsSection(
    comments: List<Comment>,
    onAddComment: (Comment) -> Unit,
    dataViewModel: DataViewModel,
    sharedPreferencesViewModel: SharedPreferencesViewModel,
    idPost: Int?,
    roomViewModel: RoomViewModel
) {
    val commentText = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("Comentarios", style = MaterialTheme.typography.titleMedium)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            items(comments) { comment ->
                Text(
                    text = "${comment.userName}: ${comment.text}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = commentText.value,
                onValueChange = { commentText.value = it },
                placeholder = { Text("Agregar un comentario...") },
                modifier = Modifier.weight(1f),
                colors = inputFieldColors()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (commentText.value.isNotBlank()) {
                        val newComment = idPost?.let {
                            Comment(
                                null,
                                it, sharedPreferencesViewModel.getUserName(), commentText.value
                            )
                        }
                        onAddComment(newComment!!)
                        var commentSaved: Comment? = null
                        coroutineScope.launch {
                            commentSaved = dataViewModel.createComment(newComment)
                        }
                        val commentLocal = CommentEntity(
                            postId = newComment.postId,
                            userName = newComment.userName,
                            text = newComment.text,
                            idBdRemote = commentSaved?.id
                        )
                        roomViewModel.insertComment(commentLocal)
                        commentText.value = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFF4CAF50).copy(0.2f)
                ),
                modifier = Modifier
                    .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(20.dp)),
            ) {
                Text("Enviar")
            }
        }
    }
}


@Composable
fun InteractionIcon(
    imageVector: ImageVector,
    contentDescription: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(24.dp)
            .clickable { onClick() },
        tint = if (isActive) activeColor else Color.Gray
    )
}


@Composable
fun MatchResultCard(matchResult: MatchResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${matchResult.homeTeam} vs ${matchResult.awayTeam}",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Resultado: ${matchResult.fullTimeScoreHome}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "- ${matchResult.fullTimeScoreAway}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun InteractionIcon(
    imageVector: ImageVector,
    contentDescription: String,
    isActive: Boolean,
    activeColor: Color
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(24.dp)
            .clickable { /*  */ },
        tint = if (isActive) activeColor else Color.Gray
    )
}

@Composable
fun SelectedPersonCard(person: PostPerson?) {
    if (person == null) {
        Text(
            text = "No se ha seleccionado ningún jugador o entrenador.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    } else {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50).copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                if (person.position == null) {
                    Text(
                        text = "Entrenador: ${person.name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(text = "Nacionalidad: ${person.nationality}", color = Color.Gray)
                    Text(text = "Fecha de nacimiento: ${person.dateOfBirth}", color = Color.Gray)
                } else {
                    Text(
                        text = "Jugador: ${person.name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(text = "Posición: ${person.position}", color = Color.Gray)
                    Text(text = "Nacionalidad: ${person.nationality}", color = Color.Gray)
                    Text(text = "Fecha de nacimiento: ${person.dateOfBirth}", color = Color.Gray)
                }
            }
        }
    }
}
