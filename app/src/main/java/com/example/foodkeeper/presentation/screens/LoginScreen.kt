package com.example.foodkeeper.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodkeeper.presentation.navigation.Routes
import com.example.foodkeeper.presentation.viewmodel.AuthState
import com.example.foodkeeper.presentation.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel(),
    navController: NavController
) {
    var email by remember { mutableStateOf("")  }
    var password by remember { mutableStateOf("")  }
    val context = LocalContext.current

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when(authState) {
            is AuthState.Authenticated -> navController.navigate(Routes.Main)
            is AuthState.Error -> Toast.makeText(
                context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()
            else -> Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Войти в аккаунт", fontSize = 32.sp)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("Электронная почта") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Пароль") }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.login(email, password)
            }
        ) {
            Text("Войти")
        }

        Spacer(Modifier.height(8.dp))

        TextButton(
            onClick = {
                navController.navigate(Routes.Register)
            }
        ) {
            Text("Еще нет аккаунта, зарегистрируйтесь")
        }

    }

}