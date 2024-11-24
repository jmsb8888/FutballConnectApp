package com.task.futballconnectapp.ui


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader() {
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
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppHeader() {
    AppHeader()
}
