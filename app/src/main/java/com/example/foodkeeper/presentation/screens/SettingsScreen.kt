package com.example.foodkeeper.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.foodkeeper.presentation.notifications.ExpiryCheckWorker
import com.example.foodkeeper.presentation.viewmodel.AuthViewModel
import com.example.foodkeeper.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout : () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val theme by settingsViewModel.theme.collectAsState()
    val notifyDays by settingsViewModel.notifyDays.collectAsState()

    if (theme == null || notifyDays == null) return
    var showLogoutDialog by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(200))
    ) {
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
                    supportingContent = { Text("За $notifyDays ${daysLabel(notifyDays!!)} до истечения срока") },
                    leadingContent = {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                    }
                )
                Slider(
                    value = notifyDays!!.toFloat(),
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
                val context = LocalContext.current


                ListItem(
                    headlineContent = {
                        Button(onClick = {
                            val testRequest = OneTimeWorkRequestBuilder<ExpiryCheckWorker>()
                                .setInputData(
                                    androidx.work.workDataOf("notify_days" to notifyDays)
                                )
                                .build()
                            WorkManager.getInstance(context).enqueue(testRequest)
                        }) {
                            Text("Тест уведомлений")
                        }
                    },
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
                            showLogoutDialog = false
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