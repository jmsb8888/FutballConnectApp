package com.task.futballconnectapp.data.bd.remote

import android.database.SQLException
import com.task.futballconnectapp.data.api.models.TeamInfo
import com.task.futballconnectapp.data.bd.models.Comment
import com.task.futballconnectapp.data.bd.models.MatchResult
import com.task.futballconnectapp.data.bd.models.Post
import com.task.futballconnectapp.data.bd.models.PostPerson
import com.task.futballconnectapp.data.bd.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.PreparedStatement
import javax.inject.Inject

class UserDao @Inject constructor() {
    suspend fun getUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            val query = "SELECT * FROM users"
            val users = mutableListOf<User>()

            try {
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery(query)
                while (resultSet.next()) {
                    val user = User(
                        id = resultSet.getInt("id"),
                        name = resultSet.getString("name"),
                        email = resultSet.getString("email"),
                        password = resultSet.getString("password"),
                        imagePerfil = resultSet.getString("imageperfil")
                    )
                    users.add(user)
                }
            } finally {
                connection.close()
            }
            users
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            val query = "SELECT * FROM users WHERE email = ?"
            var user: User? = null
            try {
                val preparedStatement = connection.prepareStatement(query)
                preparedStatement.setString(1, email)
                val resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    user = User(
                        id = resultSet.getInt("id"),
                        name = resultSet.getString("name"),
                        email = resultSet.getString("email"),
                        password = resultSet.getString("password"),
                        imagePerfil = resultSet.getString("imageperfil")
                    )
                }
            } finally {
                connection.close()
            }
            user
        }
    }

    suspend fun createPost(post: Post): Long? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            val query = """
            INSERT INTO Post (
                UserName, UserProfileImageUrl, MainImageUrl, Title, Description, 
                MatchResultId, PersonId, IsLiked
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """
            try {
                val statement: PreparedStatement =
                    connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)
                statement.setString(1, post.userName)
                statement.setString(2, post.userProfileImageUrl)
                statement.setString(3, post.mainImageUrl)
                statement.setString(4, post.title)
                statement.setString(5, post.description)
                statement.setObject(6, post.matchResult?.id)
                statement.setObject(7, post.person?.id)
                statement.setBoolean(8, false)
                statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                if (generatedKeys.next()) {
                    return@withContext generatedKeys.getLong(1)
                } else {
                    return@withContext null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            } finally {
                connection.close()
            }
        }
    }


    suspend fun getAllPosts(type: Int, idUser: Int): List<Post> {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            var query = """
            SELECT p.*, 
                   mr.HomeTeamId, mr.AwayTeamId, mr.FullTimeScoreHome, mr.FullTimeScoreAway,
                   pp.Id AS PersonId, pp.Name AS PersonName, pp.Position, pp.DateOfBirth, pp.Nationality
            FROM Post p
            LEFT JOIN MatchResult mr ON p.MatchResultId = mr.Id
            LEFT JOIN PostPerson pp ON p.PersonId = pp.Id
        """
            if (type == -1) {
                query += " WHERE p.MatchResultId IS NULL AND p.PersonId IS NOT NULL"
            } else {
                query += " WHERE p.MatchResultId IS NOT NULL AND p.PersonId IS NULL"
            }
            val posts = mutableListOf<Post>()
            try {
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery(query)
                while (resultSet.next()) {
                    val postId = resultSet.getInt("Id")
                    val matchResult = if (resultSet.getObject("MatchResultId") != null) {
                        MatchResult(
                            id = resultSet.getInt("MatchResultId"),
                            homeTeam = resultSet.getString("HomeTeamId"),
                            awayTeam = resultSet.getString("AwayTeamId"),
                            fullTimeScoreHome = resultSet.getInt("FullTimeScoreHome"),
                            fullTimeScoreAway = resultSet.getInt("FullTimeScoreAway")
                        )
                    } else null
                    val person = if (resultSet.getObject("PersonId") != null) {
                        PostPerson(
                            id = resultSet.getInt("PersonId"),
                            name = resultSet.getString("PersonName"),
                            position = resultSet.getString("Position"),
                            dateOfBirth = resultSet.getString("DateOfBirth"),
                            nationality = resultSet.getString("Nationality")
                        )
                    } else null
                    val isLiked = hasUserLikedPost(postId, idUser)
                    val post = Post(
                        idPost = postId,
                        userName = resultSet.getString("UserName"),
                        userProfileImageUrl = resultSet.getString("UserProfileImageUrl"),
                        mainImageUrl = resultSet.getString("MainImageUrl"),
                        title = resultSet.getString("Title"),
                        description = resultSet.getString("Description"),
                        matchResult = matchResult,
                        person = person,
                        isLiked = isLiked,
                    )
                    posts.add(post)
                }
            } finally {
                connection.close()
            }
            posts
        }
    }

    suspend fun getCommentsForPost(postId: Int): List<Comment> {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            val query = "SELECT UserName, Text FROM Comment WHERE PostId = ?"
            val comments = mutableListOf<Comment>()
            try {
                val statement: PreparedStatement = connection.prepareStatement(query)
                statement.setInt(1, postId)
                val resultSet = statement.executeQuery()

                while (resultSet.next()) {
                    val comment = Comment(
                        userName = resultSet.getString("UserName"),
                        text = resultSet.getString("Text"),
                        postId = postId,
                        id = null
                    )
                    comments.add(comment)
                }
            } finally {
                connection.close()
            }
            comments
        }
    }

    suspend fun createMatchResult(matchResult: MatchResult): MatchResult {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            try {
                val homeTeamId = matchResult.homeTeam
                val awayTeamId = matchResult.awayTeam
                val insertQuery = """
                INSERT INTO MatchResult (HomeTeamId, AwayTeamId, FullTimeScoreHome, FullTimeScoreAway)
                VALUES (?, ?, ?, ?);
            """
                val statement = connection.prepareStatement(
                    insertQuery,
                    java.sql.Statement.RETURN_GENERATED_KEYS
                )
                statement.setString(1, homeTeamId)
                statement.setString(2, awayTeamId)
                statement.setInt(3, matchResult.fullTimeScoreHome)
                statement.setInt(4, matchResult.fullTimeScoreAway)
                statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                if (generatedKeys.next()) {
                    val matchResultId = generatedKeys.getInt(1)
                    matchResult.copy(
                        id = matchResultId,
                        homeTeam = matchResult.homeTeam,
                        awayTeam = matchResult.awayTeam
                    )
                } else {
                    throw SQLException("Failed to retrieve ID for MatchResult")
                }
            } finally {
                connection.close()
            }
        }
    }

    private suspend fun createTeamInfo(teamInfo: TeamInfo): Int {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            try {
                val insertQuery = """
                INSERT INTO TeamInfo (ShortName, Crest)
                VALUES (?, ?);
            """
                val statement = connection.prepareStatement(
                    insertQuery,
                    java.sql.Statement.RETURN_GENERATED_KEYS
                )
                statement.setString(1, teamInfo.shortName)
                statement.setString(2, teamInfo.crest)
                statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                if (generatedKeys.next()) {
                    generatedKeys.getInt(1)
                } else {
                    throw SQLException("Failed to retrieve ID for TeamInfo")
                }
            } finally {
                connection.close()
            }
        }
    }

    suspend fun createPostPerson(postPerson: PostPerson): PostPerson {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            try {
                val insertQuery = """
                INSERT INTO PostPerson (Name, Position, DateOfBirth, Nationality)
                VALUES (?, ?, ?, ?);
            """
                val statement = connection.prepareStatement(
                    insertQuery,
                    java.sql.Statement.RETURN_GENERATED_KEYS
                )
                statement.setString(1, postPerson.name)
                statement.setString(2, postPerson.position)
                statement.setString(3, postPerson.dateOfBirth)
                statement.setString(4, postPerson.nationality)
                statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                if (generatedKeys.next()) {
                    val personId = generatedKeys.getInt(1)
                    postPerson.copy(id = personId)
                } else {
                    throw SQLException("Failed to retrieve ID for PostPerson")
                }
            } finally {
                connection.close()
            }
        }
    }

    suspend fun createUser(user: User): User {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            try {
                val insertQuery = """
                INSERT INTO users (name, email, password, imageperfil)
                VALUES (?, ?, ?, ?);
            """
                val statement = connection.prepareStatement(
                    insertQuery,
                    java.sql.Statement.RETURN_GENERATED_KEYS
                )
                statement.setString(1, user.name)
                statement.setString(2, user.email)
                statement.setString(3, user.password)
                statement.setString(4, user.imagePerfil)
                statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                if (generatedKeys.next()) {
                    val userId = generatedKeys.getInt(1)
                    user.copy(id = userId)
                } else {
                    throw SQLException("Failed to retrieve ID for User")
                }
            } finally {
                connection.close()
            }
        }
    }

    suspend fun createComment(comment: Comment): Comment {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            try {
                val insertQuery = """
                INSERT INTO Comment (PostId, UserName, Text)
                VALUES (?, ?, ?);
            """
                val statement = connection.prepareStatement(
                    insertQuery,
                    java.sql.Statement.RETURN_GENERATED_KEYS
                )
                statement.setInt(1, comment.postId)
                statement.setString(2, comment.userName)
                statement.setString(3, comment.text)
                statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                if (generatedKeys.next()) {
                    val commentId = generatedKeys.getInt(1)
                    comment.copy(id = commentId)
                } else {
                    throw SQLException("Failed to retrieve ID for Comment")
                }
            } finally {
                connection.close()
            }
        }
    }

    suspend fun addLike(postId: Int, userId: Int): Long? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            try {
                val insertQuery = """
                INSERT INTO PostLikes (PostId, UserId)
                VALUES (?, ?);
            """
                val statement = connection.prepareStatement(
                    insertQuery,
                    PreparedStatement.RETURN_GENERATED_KEYS
                )
                statement.setInt(1, postId)
                statement.setInt(2, userId)
                statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                if (generatedKeys.next()) {
                    return@withContext generatedKeys.getLong(1)
                } else {
                    return@withContext null
                }
            } catch (e: java.sql.SQLException) {
                if (e.message?.contains("duplicate key") == true) {
                    return@withContext null
                } else {
                    throw e
                }
            } finally {
                connection.close()
            }
        }
    }


    suspend fun hasUserLikedPost(postId: Int, userId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            try {
                val checkLikeQuery = """
                SELECT TOP 1 1 FROM PostLikes 
                WHERE PostId = ? AND UserId = ?
            """
                val statement = connection.prepareStatement(checkLikeQuery)
                statement.setInt(1, postId)
                statement.setInt(2, userId)
                val resultSet = statement.executeQuery()
                return@withContext resultSet.next()
            } catch (e: Exception) {
                return@withContext false
            } finally {
                connection.close()
            }
        }
    }

    suspend fun removeLike(postId: Int, userId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConfig.getConnection()
            try {
                val deleteLikeQuery = """
                DELETE FROM PostLikes WHERE PostId = ? AND UserId = ?
            """
                val statement = connection.prepareStatement(deleteLikeQuery)
                statement.setInt(1, postId)
                statement.setInt(2, userId)
                val rowsAffected = statement.executeUpdate()
                return@withContext rowsAffected > 0
            } catch (e: Exception) {
                return@withContext false
            } finally {
                connection.close()
            }
        }
    }
}
