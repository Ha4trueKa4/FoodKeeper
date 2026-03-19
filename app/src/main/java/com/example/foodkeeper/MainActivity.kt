package com.example.foodkeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.foodkeeper.presentation.screens.MainScreen
import com.example.foodkeeper.presentation.theme.FoodKeeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodKeeperTheme {
                MainScreen()
            }
        }
    }
}

