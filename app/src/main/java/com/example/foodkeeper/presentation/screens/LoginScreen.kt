package com.example.foodkeeper.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.foodkeeper.presentation.components.UserTextField
import com.example.foodkeeper.presentation.navigation.Routes


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateTo : (String) -> Unit = {inputString -> }
) {
    var emailValue by rememberSaveable { mutableStateOf("")}
    var passwordValue by rememberSaveable { mutableStateOf("")}
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            UserTextField(
                label = "Login",
                value = emailValue
            ) { inputText -> emailValue = inputText}

            UserTextField(
                label = "Login",
                value = passwordValue,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            ) { inputText -> passwordValue = inputText}

            Button(
                onClick = {}
            ) {
                Text("Login")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}

