package com.task.futballconnectapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(onLogoutClick: () -> Unit = {}) {
    Surface(
        shadowElevation = 4.dp,
        color = Color(0xFF4CAF50).copy(alpha = 0.2f)
    ) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "FutballConnect",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                        )
                    )
                }
            },
            actions = {
                TextButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.padding(end = 8.dp)
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
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppHeader() {
    AppHeader(onLogoutClick = { /* Acción de cierre de sesión */ })
}