package com.example.unilocal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.unilocal.ui.screens.WelcomeScreen
import com.example.unilocal.ui.theme.UnilocalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            UnilocalTheme {
                Surface(modifier = Modifier) {
                    WelcomeScreen()
                }
            }
        }
    }
}
