package com.example.foodkeeper

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.foodkeeper.data.local.SettingsRepository
import com.example.foodkeeper.presentation.navigation.FoodKeeperNavigation
import com.example.foodkeeper.presentation.navigation.Routes
import com.example.foodkeeper.presentation.theme.FoodKeeperTheme
import com.example.foodkeeper.presentation.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue


class MainActivity : ComponentActivity() {

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    private val settingsRepository by lazy { SettingsRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { false }

        super.onCreate(savedInstanceState) // сначала super
        requestNotificationPermission()
        enableEdgeToEdge()


        setContent {
            val isSystemDark = isSystemInDarkTheme()
            val theme by settingsRepository.theme.collectAsState(initial = "system")
            val darkTheme = when (theme) {
                "dark" -> true
                "light" -> false
                else -> isSystemDark
            }
            FoodKeeperTheme(darkTheme) {
                FoodKeeperNavigation(
                    navHostController = rememberNavController(),
                )
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                requestNotificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }
}


