package com.example.foodkeeper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodkeeper.data.local.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    val theme = repository.theme.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "light"
    )

    val notifyDays = repository.notifyDays.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        3
    )

    fun setTheme(value : String) = viewModelScope.launch { repository.setTheme(value) }
    fun setNotifyDays(value : Int) = viewModelScope.launch { repository.setNotifyDays(value) }
}