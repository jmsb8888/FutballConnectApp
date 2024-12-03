package com.task.futballconnectapp


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
import com.task.futballconnectapp.data.bd.models.Post
import com.task.futballconnectapp.data.viewmodel.RoomViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class RoomViewModelTest {
    private val mockPostDao = mockk<PostDao>(relaxed = true)
    private val mockCommentDao = mockk<CommentDao>(relaxed = true)
    private val mockPostLikesDao = mockk<PostLikesDao>(relaxed = true)
    private val mockMatchResultDao = mockk<MatchResultDao>(relaxed = true)
    private val mockPostPersonDao = mockk<PostPersonDao>(relaxed = true)
    private val mockUserDao = mockk<UserDao>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: RoomViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RoomViewModel(
            postDao = mockPostDao,
            commentDao = mockCommentDao,
            postLikesDao = mockPostLikesDao,
            matchResultDao = mockMatchResultDao,
            postPersonDao = mockPostPersonDao,
            userDao = mockUserDao
        )
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `insertPost calls insertPost on PostDao`() = runTest {
        val postEntity = PostEntity(
            id = 1,
            userName = "TestUser",
            userProfileImageUrl = "https://example.com/user.jpg",
            mainImageUrl = "https://example.com/post.jpg",
            title = "Test Title",
            description = "Test Description",
            personId = null,
            matchResultId = null,
            isLiked = false
        )
        viewModel.insertPost(postEntity)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockPostDao.insertPost(postEntity) }
    }

    @Test
    fun `getAllPostsPlayer filters and transforms posts correctly`() = runTest {
        val mockPosts = listOf(
            PostEntity(
                id = 1,
                userName = "TestUser",
                userProfileImageUrl = "https://example.com/user.jpg",
                mainImageUrl = "https://example.com/post.jpg",
                title = "Title 1",
                description = "Description 1",
                personId = 101,
                matchResultId = null,
                isLiked = false
            )
        )
        val mockPostPerson = PostPersonEntity(
            id = 101,
            name = "Player 1",
            position = "Forward",
            dateOfBirth = "1990-01-01",
            nationality = "Country A"
        )
        val userId = 123

        coEvery { mockPostDao.getAllPosts() } returns mockPosts
        coEvery { mockPostPersonDao.getPostPersonById(101) } returns mockPostPerson
        coEvery { mockPostLikesDao.getPostLike(1, userId) } returns null
        var result: List<Post>? = null
        viewModel.getAllPostsPlayer(userId) { posts ->
            result = posts
        }
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, result?.size)
        val post = result?.first()
        assertEquals("TestUser", post?.userName)
        assertEquals("Player 1", post?.person?.name)
        assertEquals(false, post?.isLiked)
    }
    @Test
    fun `getAllPostsMatches filters and transforms match posts correctly`() = runTest {
        val mockPosts = listOf(
            PostEntity(
                id = 2,
                userName = "MatchUser",
                userProfileImageUrl = "https://example.com/matchuser.jpg",
                mainImageUrl = "https://example.com/match.jpg",
                title = "Match Title",
                description = "Match Description",
                personId = null,
                matchResultId = 201,
                isLiked = false
            )
        )
        val mockMatchResult = MatchResultEntity(
            id = 201,
            homeTeamId = "Team A",
            awayTeamId = "Team B",
            fullTimeScoreHome = 2,
            fullTimeScoreAway = 1
        )
        val userId = 456
        coEvery { mockPostDao.getAllPosts() } returns mockPosts
        coEvery { mockMatchResultDao.getMatchResultById(201) } returns mockMatchResult
        coEvery { mockPostLikesDao.getPostLike(2, userId) } returns null
        var result: List<Post>? = null
        viewModel.getAllPostsMatches(userId) { posts ->
            result = posts
        }
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, result?.size)
        val post = result?.first()
        assertEquals("MatchUser", post?.userName)
        assertEquals("Team A", post?.matchResult?.homeTeam)
        assertEquals(false, post?.isLiked)
    }

    @Test
    fun `deletePost calls deletePost on PostDao`() = runTest {
        val postEntity = PostEntity(
            id = 3,
            userName = "ToDeleteUser",
            userProfileImageUrl = "https://example.com/delete.jpg",
            mainImageUrl = "https://example.com/deletepost.jpg",
            title = "Delete Title",
            description = "Delete Description",
            personId = null,
            matchResultId = null,
            isLiked =  false
        )
        viewModel.deletePost(postEntity)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockPostDao.deletePost(postEntity) }
    }

    @Test
    fun `insertComment calls insertComment on CommentDao`() = runTest {
        val commentEntity = CommentEntity(
            id = 4,
            postId = 1,
            userName = "CommentUser",
            text = "Test Comment"
        )
        viewModel.insertComment(commentEntity)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockCommentDao.insertComment(commentEntity) }
    }

    @Test
    fun `getCommentsForPost retrieves and transforms comments correctly`() = runTest {
        val postId = 1
        val mockComments = listOf(
            CommentEntity(id = 1, postId = postId, userName = "User1", text = "Comment 1"),
            CommentEntity(id = 2, postId = postId, userName = "User2", text = "Comment 2")
        )
        coEvery { mockCommentDao.getCommentsForPost(postId) } returns mockComments
        var result: List<Comment>? = null
        viewModel.getCommentsForPost(postId) { comments ->
            result = comments
        }
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(2, result?.size)
        assertEquals("User1", result?.first()?.userName)
        assertEquals("Comment 1", result?.first()?.text)
    }

    @Test
    fun `likePost calls insertPostLike on PostLikesDao`() = runTest {
        val postLikesEntity = PostLikesEntity(
            id = 5,
            postId = 1,
            userId = 123
        )
        viewModel.likePost(postLikesEntity)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockPostLikesDao.insertPostLike(postLikesEntity) }
    }

    @Test
    fun `unlikePost calls deletePostLike on PostLikesDao`() = runTest {
        val postLikesEntity = PostLikesEntity(
            id = 6,
            postId = 1,
            userId = 123
        )
        viewModel.unlikePost(postLikesEntity)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockPostLikesDao.deletePostLike(postLikesEntity) }
    }

    @Test
    fun `validateUser returns correct user when credentials are valid`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val mockUserEntity = UserEntity(
            id = 7,
            email = email,
            password = password,
            name = "Test User"
        )
        coEvery { mockUserDao.getUserByEmail(email) } returns mockUserEntity
        var result: UserEntity? = null
        viewModel.validateUser(email, password) { user ->
            result = user
        }
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(mockUserEntity, result)
    }
}
