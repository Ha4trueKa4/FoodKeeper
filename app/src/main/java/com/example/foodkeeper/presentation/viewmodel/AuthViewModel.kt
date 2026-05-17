package com.example.foodkeeper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodkeeper.data.local.ProductRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

class AuthViewModel(
    private val auth: FirebaseAuth,
    private val repository: ProductRepositoryImpl
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
            syncProductsFromFirebase()
        }
    }

    fun login(email: String, password: String) {
        if (!validateInput(email, password)) {
            _authState.value = AuthState.Error("Проверь корректность email и пароля")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                withTimeoutOrNull(10.seconds) {
                    auth.signInWithEmailAndPassword(email, password).await()
                }
                syncProductsFromFirebase()
                _authState.value = AuthState.Authenticated
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _authState.value = AuthState.Error("Неверная почта или пароль")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Ошибка входа")
            }
        }
    }

    fun register(email: String, password: String) {
        if (!validateInput(email, password)) {
            _authState.value = AuthState.Error("Проверь корректность email и пароля")
            return
        }

        if (password.length < 6) {
            _authState.value = AuthState.Error("Пароль должен содержать минимум 6 символов")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                withTimeoutOrNull(10.seconds) {
                    auth.createUserWithEmailAndPassword(email, password).await()
                }
                syncProductsFromFirebase()
                _authState.value = AuthState.Authenticated
            } catch (e: FirebaseAuthWeakPasswordException) {
                _authState.value = AuthState.Error("Слабый пароль. Используй буквы, цифры и спецсимволы")
            } catch (e: FirebaseAuthUserCollisionException) {
                _authState.value = AuthState.Error("Этот email уже зарегистрирован")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Ошибка регистрации")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                clearAllProducts()
                auth.signOut()
                _authState.value = AuthState.Unauthenticated
            } catch (e: Exception) {
                auth.signOut()
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    private fun syncProductsFromFirebase() {
        viewModelScope.launch {
            try {
                repository.syncFromFirestore()
            } catch (e: Exception) {
                // Sync failed, will retry through SyncWorker
            }
        }
    }

    private suspend fun clearAllProducts() {
        try {
            repository.clearAllProducts()
        } catch (e: Exception) {
            // Ignore
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return email.isNotEmpty() &&
                email.contains("@") &&
                password.isNotEmpty()
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

