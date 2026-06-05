package com.example.foodkeeper.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodkeeper.data.local.dao.ProductDao
import com.example.foodkeeper.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 6, exportSchema = false)
abstract class FoodKeeperDatabase : RoomDatabase() {
    abstract fun getProductDao() : ProductDao
}
