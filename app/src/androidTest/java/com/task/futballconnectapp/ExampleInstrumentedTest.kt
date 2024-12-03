package com.task.futballconnectapp.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasClickAction
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.task.futballconnectapp.data.bd.local.dao.CommentDao
import com.task.futballconnectapp.data.bd.local.dao.MatchResultDao
import com.task.futballconnectapp.data.bd.local.dao.PostDao
import com.task.futballconnectapp.data.bd.local.dao.PostLikesDao
import com.task.futballconnectapp.data.bd.local.dao.PostPersonDao
import com.task.futballconnectapp.data.bd.local.dao.UserDao
import com.task.futballconnectapp.data.viewmodel.ApiViewModel
import com.task.futballconnectapp.data.viewmodel.DataViewModel
import com.task.futballconnectapp.data.viewmodel.RoomViewModel
import com.task.futballconnectapp.data.viewmodel.SharedPreferencesViewModel
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

class UserRegistrationScreenTest {
    private val sharedPreferencesViewModel: SharedPreferencesViewModel
        get() {
            TODO()
        }
    private val dataViewModel: DataViewModel
        get() {
            TODO()
        }
    private val roomViewModel: RoomViewModel
        get() {
            TODO()
        }
    private val apiViewModel: ApiViewModel
        get() {
            TODO()
        }
private val navController = testRegistrationForm()
    @Inject
    lateinit var postDao: PostDao
    @Inject
    lateinit var commentDao: CommentDao
    @Inject
    lateinit var postLikesDao: PostLikesDao
    @Inject
    lateinit var matchResultDao: MatchResultDao
    @Inject
    lateinit var postPersonDao: PostPersonDao
    @Inject
    lateinit var userDao: UserDao
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRegistrationForm() {
        // Prepara los mocks de los ViewModels
        val apiViewModel = apiViewModel
        val dataViewModel = dataViewModel
        val sharedPreferencesViewModel = sharedPreferencesViewModel
        val roomViewModel = roomViewModel
        composeTestRule.setContent{
        val navController = rememberNavController()
            UserRegistrationScreen(
                navController = navController,
                apiViewModel = apiViewModel,
                dataViewModel = dataViewModel,
                sharedPreferencesViewModel = sharedPreferencesViewModel,
                roomViewModel = roomViewModel
            )
        }

        // Verificar que el título esté presente
        composeTestRule.onNodeWithText("Registro de Usuario").assertExists()

        // Verificar que los campos de texto están presentes
        composeTestRule.onNodeWithText("Nombre").assertExists()
        composeTestRule.onNodeWithText("Apellido").assertExists()
        composeTestRule.onNodeWithText("Correo electrónico").assertExists()
        composeTestRule.onNodeWithText("Contraseña").assertExists()
        composeTestRule.onNodeWithText("Confirmar contraseña").assertExists()

        // Verificar que el botón de registro está presente
        composeTestRule.onNodeWithText("Registrar").assertExists()

        // Llenar los campos
        composeTestRule.onNodeWithText("Nombre").performTextInput("Juan")
        composeTestRule.onNodeWithText("Apellido").performTextInput("Perez")
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("juan@example.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password123")
        composeTestRule.onNodeWithText("Confirmar contraseña").performTextInput("password123")

    }
    @Test
    fun testLoginScreenUI() {
        composeTestRule.setContent {
            LoginScreen(
                navController = rememberNavController(),
                dataViewModel = dataViewModel,
                sharedPreferencesViewModel = sharedPreferencesViewModel,
                roomViewModel = roomViewModel
            )
        }

        // Verificar que los textos estáticos están presentes
        composeTestRule.onNodeWithText("Iniciar Sesión").assertExists()
        composeTestRule.onNodeWithText("Usuario").assertExists()
        composeTestRule.onNodeWithText("Contraseña").assertExists()
        composeTestRule.onNodeWithText("Iniciar Sesión").assertExists()
        composeTestRule.onNodeWithText("¿No tienes cuenta? Crear una nueva").assertExists()

        // Verificar que los campos de texto y el botón están presentes
        composeTestRule.onNodeWithTag("usernameField").assertExists()
        composeTestRule.onNodeWithTag("passwordField").assertExists()
        composeTestRule.onNodeWithTag("loginButton").assertExists()
    }

}
