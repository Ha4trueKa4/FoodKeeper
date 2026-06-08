package com.example.foodkeeper.domain

enum class StorageLocation(val displayName: String, val emoji: String) {
    FRIDGE("Холодильник", "❄️"),
    FREEZER("Морозилка", "🧊"),
    PANTRY("Кладовая", "🚪"),
    OTHER("Другое", "📦")
}