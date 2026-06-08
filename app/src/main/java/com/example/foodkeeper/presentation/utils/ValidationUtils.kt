package com.example.foodkeeper.presentation.utils

object ValidationUtils {

    private val emailRegex = """^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$""".toRegex()

    fun validateEmail(email: String): String = when {
        email.isBlank() -> "Email не может быть пустым"
        email.length < 5 -> "Email слишком короткий"
        !emailRegex.matches(email) -> "Некорректный формат email"
        else -> ""
    }

    fun validatePassword(password: String, isRegister: Boolean = false): String = when {
        password.isBlank() -> "Пароль не может быть пустым"
        password.length < 6 -> "Минимум 6 символов"
        password.length > 128 -> "Пароль слишком длинный"
        isRegister && !password.any { it.isDigit() } -> "Пароль должен содержать хотя бы одну цифру"
        isRegister && !password.any { it.isLetter() } -> "Пароль должен содержать хотя бы одну букву"
        else -> ""
    }
}


