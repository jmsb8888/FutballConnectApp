
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.task.futballconnectapp.data.viewmodel.DataViewModel
import com.task.futballconnectapp.data.viewmodel.SharedPreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    onLogoutClick: () -> Unit = {},
    navController: NavController,
    dataViewModel: DataViewModel,
    sharedPreferencesViewModel: SharedPreferencesViewModel
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route
    val isOnLoginOrRegister = currentRoute == "login" || currentRoute == "register"
    Surface(
        shadowElevation = 4.dp,
        color = Color(0xFF4CAF50).copy(alpha = 0.2f)
    ) {
        TopAppBar(
            title = {
                ConstraintLayout(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val (title, button) = createRefs()
                    Text(
                        text = "FutballConnect",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                        ),
                        modifier = Modifier.constrainAs(title) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start, margin = 16.dp)
                        }
                    )
                    if (!isOnLoginOrRegister) {
                        TextButton(
                            onClick = {
                                sharedPreferencesViewModel.clearUserId()
                                navController.navigate("login")
                                onLogoutClick()
                            },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .constrainAs(button) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                }
                        ) {
                            Text(
                                text = "Cerrar sesión",
                                color = Color(0xFF4CAF50),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent
            ),
        )
    }
}