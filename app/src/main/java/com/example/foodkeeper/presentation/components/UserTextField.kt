package com.example.foodkeeper.presentation.components

import android.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun UserTextField(
    modifier: Modifier = Modifier,
    label : String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    value: String = "",
    onValueChange : (String) -> Unit
) {
    TextField(
        value = value,
        textStyle = MaterialTheme.typography.bodyMedium,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation
    )
}