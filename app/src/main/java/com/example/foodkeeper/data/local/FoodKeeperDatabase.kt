package com.example.foodkeeper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodkeeper.data.local.dao.ProductDao
import com.example.foodkeeper.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 8, exportSchema = false)
abstract class FoodKeeperDatabase : RoomDatabase() {
    abstract fun getProductDao() : ProductDao
}
