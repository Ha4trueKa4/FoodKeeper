package com.example.foodkeeper.presentation.screens
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.presentation.components.ExpiryDatePicker
import com.example.foodkeeper.presentation.components.ImagePickerButton
import com.example.foodkeeper.presentation.viewmodel.FoodKeeperViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import androidx.core.net.toUri
import com.example.foodkeeper.data.remote.ImageStorageDataSource
import com.example.foodkeeper.domain.Category
import com.example.foodkeeper.domain.StorageLocation
import com.example.foodkeeper.domain.Units
import com.example.foodkeeper.presentation.components.DropdownField
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    modifier: Modifier = Modifier,
    viewModel: FoodKeeperViewModel = koinViewModel(),
    productId: String?,
    onNavigateBack: () -> Unit,
) {
    val isEditMode = productId != null
    var name by rememberSaveable { mutableStateOf("") }
    var expiryDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var category by rememberSaveable { mutableStateOf(Category.OTHER) }
    var quantity by rememberSaveable { mutableStateOf("1") }
    var unit by rememberSaveable { mutableStateOf(Units.PCS) }
    var storageLocation by rememberSaveable { mutableStateOf(StorageLocation.FRIDGE) }
    var notes by rememberSaveable { mutableStateOf("") }

    val imageStorage: ImageStorageDataSource = koinInject()

    LaunchedEffect(productId) {
        if (productId != null) {
            val product = viewModel.getProductById(productId)
            if (product != null) {
                name = product.name
                expiryDate = product.expiryDate
                if (product.imageUrl.isNotBlank()) imageUri = product.imageUrl.toUri()
                category = product.category
                quantity = product.quantity.toString()
                unit = product.unit
                storageLocation = product.storageLocation
                notes = product.notes
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    fun onSubmit() {
        if (isLoading) return
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) { errorMessage = "Введите название продукта"; return }
        if (expiryDate == null) { errorMessage = "Выберите дату"; return }
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        isLoading = true
        coroutineScope.launch {
            try {
                val finalImageUrl = if (imageUri != null && !imageUri.toString().startsWith("https://")) {
                    try {
                        withTimeoutOrNull(5000) { // 5 секунд максимум
                            imageStorage.uploadImage(imageUri!!)
                        } ?: imageUri?.toString() ?: "" // если таймаут — сохраняем локальный uri
                    } catch (_: Exception) {
                        imageUri?.toString() ?: ""
                    }
                } else {
                    imageUri?.toString() ?: ""
                }

                val product = Product(
                    firebaseId = if (isEditMode) productId else UUID.randomUUID().toString(),
                    name = trimmedName,
                    expiryDate = expiryDate!!,
                    imageUrl = finalImageUrl,
                    userId = userId,
                    category = category,
                    quantity = quantity.toFloatOrNull() ?: 1f,
                    unit = unit,
                    storageLocation = storageLocation,
                    notes = notes.trim(),
                )

                if (isEditMode) viewModel.updateProduct(product)
                else viewModel.addProductAndAwait(product)
                onNavigateBack()
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) "Редактировать" else "Новый продукт",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ImagePickerButton(
                selectedImageUri = imageUri,
                onImageSelected = { uri -> imageUri = uri; errorMessage = null }
            )

            // Название
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it; errorMessage = null },
                label = { Text("Название продукта") },
                singleLine = true,
                isError = errorMessage != null && name.trim().isEmpty(),
                supportingText = {
                    if (errorMessage != null && name.trim().isEmpty()) Text(errorMessage!!)
                },
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) }
            )

            // Количество + единица
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = quantity,
                    onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) quantity = it },
                    label = { Text("Количество") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                DropdownField(
                    modifier = Modifier.weight(1f),
                    label = "Единица",
                    selected = unit.displayName,
                    options = Units.entries.map { it.displayName },
                    onSelected = { name -> unit = Units.entries.first { it.displayName == name } }
                )
            }

            // Категория
            DropdownField(
                modifier = Modifier.fillMaxWidth(),
                label = "Категория",
                selected = "${category.emoji} ${category.displayName}",
                options = Category.entries.map { "${it.emoji} ${it.displayName}" },
                onSelected = { name -> category = Category.entries.first { "${it.emoji} ${it.displayName}" == name } }
            )

            // Место хранения
            DropdownField(
                modifier = Modifier.fillMaxWidth(),
                label = "Место хранения",
                selected = "${storageLocation.emoji} ${storageLocation.displayName}",
                options = StorageLocation.entries.map { "${it.emoji} ${it.displayName}" },
                onSelected = { name -> storageLocation = StorageLocation.entries.first { "${it.emoji} ${it.displayName}" == name } }
            )

            // Срок годности
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (errorMessage != null && expiryDate == null)
                        MaterialTheme.colorScheme.errorContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("Срок годности", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                if (expiryDate != null) formatDate(expiryDate!!) else "Не выбрано",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    ExpiryDatePicker(selectedDateMillis = expiryDate) { millis ->
                        expiryDate = millis; errorMessage = null
                    }
                }
            }

            if (errorMessage != null && expiryDate == null) {
                Text(errorMessage!!, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 4.dp))
            }

            // Заметка
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Заметка") },
                placeholder = { Text("Необязательно...") },
                minLines = 1,
                maxLines = 4,
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = null) }
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = ::onSubmit,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (isEditMode) "Сохранить" else "Добавить", style = MaterialTheme.typography.labelLarge)
                }
            }

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Отмена", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
private   fun formatDate(timeMillis: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.forLanguageTag("ru-RU"))
    return dateFormat.format(Date(timeMillis))
}

