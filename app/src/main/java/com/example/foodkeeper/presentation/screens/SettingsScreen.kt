package com.example.foodkeeper.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodkeeper.presentation.viewmodel.AuthViewModel
import com.example.foodkeeper.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val theme by settingsViewModel.theme.collectAsState()
    val notifyDays by settingsViewModel.notifyDays.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            SettingsSectionHeader("Уведомления")

            ListItem(
                headlineContent = { Text("Предупреждать за") },
                supportingContent = { Text("За $notifyDays ${daysLabel(notifyDays)} до истечения срока") },
                leadingContent = {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                }
            )
            Slider(
                value = notifyDays.toFloat(),
                onValueChange = { settingsViewModel.setNotifyDays(it.toInt()) },
                valueRange = 1f..14f,
                steps = 12,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingsSectionHeader("Внешний вид")

            ListItem(
                headlineContent = { Text("Тема") },
                leadingContent = {
                    Icon(Icons.Default.Palette, contentDescription = null)
                },
                trailingContent = {
                    SingleChoiceSegmentedButtonRow {
                        listOf(
                            "light" to "☀️",
                            "system" to "⚙️",
                            "dark" to "🌙"
                        ).forEachIndexed { index, (value, label) ->
                            SegmentedButton(
                                selected = theme == value,
                                onClick = { settingsViewModel.setTheme(value) },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = 3
                                ),
                                label = { Text(label) }
                            )
                        }
                    }
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingsSectionHeader("Аккаунт")

            ListItem(
                headlineContent = { Text("Электронная почта") },
                supportingContent = { Text(authViewModel.userEmail) },
                leadingContent = {
                    Icon(Icons.Default.Person, contentDescription = null)
                }
            )

            ListItem(
                headlineContent = {
                    Text("Выйти из аккаунта", color = MaterialTheme.colorScheme.error)
                },
                leadingContent = {
                    Icon(
                        Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                modifier = Modifier.clickable { showLogoutDialog = true }
            )
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Выйти из аккаунта?") },
            text = { Text("Вы уверены? Для входа потребуется повторная авторизация.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        authViewModel.signOut()
                        onLogout()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Выйти") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Отмена") }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

private fun daysLabel(days: Int): String = when {
    days % 10 == 1 && days % 100 != 11 -> "день"
    days % 10 in 2..4 && days % 100 !in 12..14 -> "дня"
    else -> "дней"
}