package com.example.foodkeeper.domain

import com.example.foodkeeper.R

enum class Category(val displayName: String, val emoji: String, val placeholder: Int) {
    DAIRY("Молочное", "🥛", R.drawable.placeholder_diary),
    MEAT("Мясо и рыба", "🥩", R.drawable.placeholder_meat),
    VEGETABLES("Овощи и фрукты", "🥦", R.drawable.placeholder_vegetables),
    BAKERY("Хлеб и выпечка", "🍞", R.drawable.placeholder_bakery),
    CANNED("Консервы", "🥫", R.drawable.placeholder_canned),
    DRINKS("Напитки", "🧃", R.drawable.placeholder_drinks),
    FROZEN("Заморозка", "🧊", R.drawable.placeholder_frozen),
    OTHER("Прочее", "🍳", R.drawable.placeholder_other)
}

enum class Units(val displayName: String) {
    PCS("шт"),
    GRAMS("г"),
    KG("кг"),
    ML("мл"),
    LITERS("л"),
    PACK("уп")
}

enum class StorageLocation(val displayName: String, val emoji: String) {
    FRIDGE("Холодильник", "❄️"),
    FREEZER("Морозилка", "🧊"),
    PANTRY("Кладовая", "🚪"),
    OTHER("Другое", "📦")
}