package com.task.futballconnectapp.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.task.futballconnectapp.R


@Composable
fun FootballPostsScreen(navController: NavController, posts: List<Post>, screen: String) {
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
                PostCard(post)
            }
        }
    }
}

@Composable
fun PostCard(post: Post) {
    val isLikedState = remember { mutableStateOf(post.isLiked) }
    val showComments = remember { mutableStateOf(false) }
    val comments = remember { mutableStateListOf<Comment>() }
    comments += post.comments

    Box(
        modifier = Modifier.clickable { /*  */ }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                //containerColor = Color(0xFF4CAF50).copy(alpha = 0.2f)
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
                        Log.e("----MatchResult", "Mostrando match result")
                        MatchResultCard(matchResult = post.matchResult)
                    } else if (post.person != null) {
                        Log.e("----MatchResult", "Mostrando persona: ${post.person}")
                        SelectedPersonCard(person = post.person)
                    } else {
                        Log.e("----MatchResult", "No hay match result ni persona")
                    }

                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    InteractionIcon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Me gusta",
                        isActive = isLikedState.value,
                        activeColor = Color(0xFF4CAF50),
                        onClick = { isLikedState.value = !isLikedState.value }
                    )
                    InteractionIcon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = "Comentar",
                        isActive = false,
                        activeColor = Color.Gray,
                        onClick = { showComments.value = true }
                    )
                    InteractionIcon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir",
                        isActive = false,
                        activeColor = Color.Gray,
                        onClick = { /*  */ }
                    )
                }
                if (showComments.value) {
                    Log.d("Comentarios", "Mostrando ${post.comments} comentarios")
                    CommentsSection(comments = comments) { newComment ->
                        comments.add(newComment)
                    }
                }
            }
        }
    }
}

@Composable
fun CommentsSection(comments: List<Comment>, onAddComment: (Comment) -> Unit) {
    val commentText = remember { mutableStateOf("") }

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
                        onAddComment(Comment("Usuario", commentText.value))
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
            text = "${matchResult.homeTeam.shortName} vs ${matchResult.awayTeam.shortName}",
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
    Log.e("----MatchResultr", "${person}")

    if (person == null) {
        Log.e("----MatchResultr222", "${person}")

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
        Log.e("----MatchResultr444", "${person}")

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
                Log.e("----MatchResultr5555", "${person}")
                if (person.position == null) {
                    Text(
                        text = "Entrenador: ${person.name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(text = "Nacionalidad: ${person.nationality}", color = Color.Gray)
                    Text(text = "Fecha de nacimiento: ${person.dateOfBirth}", color = Color.Gray)
                }
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

data class Post(
    val userName: String,
    val userProfileImageUrl: String,
    val mainImageUrl: String,
    val title: String,
    val description: String,
    val matchResult: MatchResult? = null,
    val person: PostPerson? = null,
    val isLiked: Boolean,
    val comments: List<Comment> = listOf()

)

data class Comment(val userName: String, val text: String)


data class PostPerson(
    val id: Int,
    val name: String,
    val position: String? = null,
    val dateOfBirth: String,
    val nationality: String
)
