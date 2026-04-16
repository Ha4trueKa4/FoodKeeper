package com.example.foodkeeper.presentation.screens
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.presentation.components.ExpiryDatePicker
import com.example.foodkeeper.presentation.components.ImagePickerButton
import com.example.foodkeeper.presentation.viewmodel.FoodKeeperViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    modifier: Modifier = Modifier,
    viewModel: FoodKeeperViewModel = koinViewModel(),
    productId: Int?,
    onNavigateBack: () -> Unit,
) {
    val isEditMode = productId != null
    var name by rememberSaveable { mutableStateOf("") }
    var expiryDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(productId) {
        if (productId != null) {
            val product = viewModel.getProductById(productId)
            if (product != null) {
                name = product.name
                expiryDate = product.expiryDate
                if (product.imageUrl.isNotBlank()) {
                    imageUri = Uri.parse(product.imageUrl)
                }
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
            errorMessage = "Выберите дату"
            return
        }
        val product = Product(
            id = productId ?: 0,
            name = trimmedName,
            expiryDate = expiryDate!!,
            imageUrl = imageUri?.toString() ?: ""
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
                title = {
                    Text(
                        if (isEditMode) "Редактировать" else "Добавить продукт",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ImagePickerButton(
                selectedImageUri = imageUri,
                onImageSelected = { uri ->
                    imageUri = uri
                    if (errorMessage != null) errorMessage = null
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { 
                    name = it
                    if (errorMessage != null) errorMessage = null
                },
                label = { Text("Название продукта") },
                placeholder = { Text("Молоко, Хлеб...") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                ),
                isError = errorMessage != null && name.trim().isEmpty()
            )

            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExpiryDatePicker(
                    selectedDateMillis = expiryDate
                ) { millis ->
                    expiryDate = millis
                    if (errorMessage != null) errorMessage = null
                }
                Spacer(Modifier.width(20.dp))
                if (expiryDate != null) {
                    Text(
                        text = "Дата истечения срока годности: ${formatDate(expiryDate!!)}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }


            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 16.sp,
                )
            }

            Button(
                onClick = ::onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = if (isEditMode) "Сохранить" else "Добавить",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Отмена",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private fun formatDate(timeMillis: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.forLanguageTag("ru-RU"))
    return dateFormat.format(Date(timeMillis))
}

