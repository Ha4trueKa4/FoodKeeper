package com.example.foodkeeper.domain

enum class Category(val displayName: String, val emoji: String) {
    DAIRY("Молочное", "🥛"),
    MEAT("Мясо и рыба", "🥩"),
    VEGETABLES("Овощи и фрукты", "🥦"),
    BAKERY("Хлеб и выпечка", "🍞"),
    CANNED("Консервы", "🥫"),
    DRINKS("Напитки", "🧃"),
    FROZEN("Заморозка", "🧊"),
    OTHER("Прочее", "🍳")
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