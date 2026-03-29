package com.example.foodkeeper.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodkeeper.data.local.dao.ProductDao
import com.example.foodkeeper.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class FoodKeeperDatabase : RoomDatabase() {
    abstract fun getProductDao() : ProductDao

    companion object {
        @Volatile
        private var INSTANCE : FoodKeeperDatabase? = null

        fun getDataBase(context: Context): FoodKeeperDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodKeeperDatabase::class.java,
                    "food_keeper_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
