package com.example.foodkeeper.domain

enum class Category(val displayName: String, val emoji: String) {
    DAIRY("Молочное", "🥛"),
    MEAT("Мясо и рыба", "🥩"),
    VEGETABLES("Овощи и фрукты", "🥦"),
    BAKERY("Хлеб и выпечка", "🍞"),
    CANNED("Консервы", "🥫"),
    DRINKS("Напитки", "🧃"),
    FROZEN("Заморозка", "🧊"),
    OTHER("Прочее", "🍳"),
    SWEET("Сладкое", "🍬")
}