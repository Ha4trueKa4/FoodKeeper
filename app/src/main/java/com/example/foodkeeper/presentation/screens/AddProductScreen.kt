package com.example.foodkeeper.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.presentation.components.ExpiryDatePicker
import com.example.foodkeeper.presentation.viewmodel.FoodKeeperViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    modifier: Modifier = Modifier,
    viewModel: FoodKeeperViewModel = koinViewModel(),
    productId : Int?,
    onNavigateBack : () -> Unit,
) {

    val isEditMode = productId != null

    var name by rememberSaveable { mutableStateOf("") }
    var expiryDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(productId) {
        if (productId != null) {
            val product = viewModel.getProductById(productId)
            if (product != null) {
                name = product.name
                expiryDate = product.expiryDate
            }
        }
    }



    val coroutineScope = rememberCoroutineScope()

    fun onSubmit() {
        val trimmedName = name.trim()

        if (trimmedName.isEmpty()) {
            errorMessage = "Введите название продукта"
            return
        }
        if (expiryDate == null) {
            errorMessage = "Выберите дату истечения срока годности"
            return
        }
        val product = Product(
            id = productId ?: 0,
            name =trimmedName,
            expiryDate= expiryDate!!,
            imageUrl = ""
        )

        coroutineScope.launch {
            if (isEditMode) {
                viewModel.updateProduct(product)
            } else {
                viewModel.addProductAndAwait(product)
            }
            onNavigateBack()
        }

    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(
                    if (productId != null) "Редактировать продкут" else "Добавить продукт"
                ) },
                navigationIcon = { IconButton(
                    onClick = onNavigateBack
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }}
            )
        },
    ) { innerPadding ->
        Column (
            modifier= Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                singleLine = true
            )

            ExpiryDatePicker(
                selectedDateMillis = expiryDate
            ) { millis -> expiryDate = millis }

            expiryDate?.let {
                Text("$it")
            }

            if (errorMessage != null) {
                Text(text = errorMessage ?: "", color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = ::onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled = name.trim().isNotEmpty() && expiryDate != null
            ) {
                Text(
                    if (isEditMode) "Сохранить изменения"
                    else "Сохранить"
                )
            }
        }
    }
}