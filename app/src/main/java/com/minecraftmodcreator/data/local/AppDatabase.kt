package com.minecraftmodcreator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.minecraftmodcreator.data.local.dao.ModDao
import com.minecraftmodcreator.data.local.entity.ModEntity

@Database(
    entities = [ModEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun modDao(): ModDao
}
