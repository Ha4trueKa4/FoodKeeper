package com.example.foodkeeper.presentation.components

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpiryDatePicker(
    modifier: Modifier = Modifier,
    selectedDateMillis : Long?,
    onDatePick: (Long) -> Unit,

) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis
    )

    val formattedDate = selectedDateMillis?.let {
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(it))
    } ?: "Выбрать дату"

    Button(onClick = {showDatePicker = true}) { Text(formattedDate) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false},
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onDatePick(millis)

                        }
                        showDatePicker=false
                    }
                ) {
                    Text("Ок")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            },
        ) {
            DatePicker(datePickerState)
        }
    }
}