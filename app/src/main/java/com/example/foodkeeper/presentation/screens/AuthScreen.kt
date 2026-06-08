package com.example.foodkeeper.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodkeeper.presentation.utils.ValidationUtils
import com.example.foodkeeper.presentation.viewmodel.AuthState
import com.example.foodkeeper.presentation.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onNavigateTo : () -> Unit
) {
    var isLoginMode by remember { mutableStateOf(true) }
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var firebaseError by remember { mutableStateOf("") }
    
    var isLoading by remember { mutableStateOf(false) }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                onNavigateTo()
                isLoading = false
                hasAttemptedSubmit = false
                email = ""
                password = ""
                emailError = ""
                passwordError = ""
            }
            is AuthState.Error -> {
                isLoading = false
                firebaseError = (authState as AuthState.Error).message
            }
            is AuthState.Loading -> {
                isLoading = true
                firebaseError = ""
            }
            else -> {}
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                if (isLoginMode) "Войти" else "Создать аккаунт",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    firebaseError = ""
                    if (hasAttemptedSubmit) {
                        emailError = ValidationUtils.validateEmail(it)
                    }
                },
                label = {
                    Text(
                        emailError.ifEmpty { "Электронная почта" },
                        color = if (emailError.isNotEmpty()) Color.Red else Color.Unspecified
                    )
                },
                leadingIcon = { Icon(Icons.Rounded.AccountCircle, contentDescription = null) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (emailError.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = if (emailError.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    errorContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                singleLine = true,
                enabled = !isLoading,
                isError = emailError.isNotEmpty()
            )
            
            Spacer(Modifier.height(8.dp))
            
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    firebaseError = ""
                    if (hasAttemptedSubmit) {
                        passwordError = ValidationUtils.validatePassword(it, isRegister = !isLoginMode)
                    }
                },
                label = {
                    Text(
                        passwordError.ifEmpty { "Пароль" },
                        color = if (passwordError.isNotEmpty()) Color.Red else Color.Unspecified
                    )
                },
                leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
                shape = RoundedCornerShape(8.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    Icon(
                        imageVector = icon,
                        contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль",
                        modifier = Modifier
                            .clickable(onClick = { passwordVisible = !passwordVisible })
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (emailError.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = if (emailError.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    errorContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                singleLine = true,
                enabled = !isLoading,
                isError = passwordError.isNotEmpty()
            )
            
            Spacer(Modifier.height(24.dp))
            
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        hasAttemptedSubmit = true
                        emailError = ValidationUtils.validateEmail(email)
                        passwordError = ValidationUtils.validatePassword(password, isRegister = !isLoginMode)
                        
                        if (emailError.isEmpty() && passwordError.isEmpty()) {
                            if (isLoginMode) {
                                viewModel.login(email, password)
                            } else {
                                viewModel.register(email, password)
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text(if (isLoginMode) "Войти" else "Зарегистрироваться")
                }

            }

            if (firebaseError.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = firebaseError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            
            Spacer(Modifier.height(16.dp))
            
            TextButton(
                onClick = { 
                    isLoginMode = !isLoginMode
                    email = ""
                    password = ""
                    emailError = ""
                    passwordError = ""
                    hasAttemptedSubmit = false
                },
                enabled = !isLoading
            ) {
                Text(
                    if (isLoginMode) 
                        "Нет аккаунта? Зарегистрироваться" 
                    else 
                        "Уже есть аккаунт? Войти"
                )
            }
        }
    }
}

