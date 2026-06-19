package com.minecraftmodcreator.di

import android.content.Context
import androidx.room.Room
import com.minecraftmodcreator.data.local.AppDatabase
import com.minecraftmodcreator.data.local.dao.ModDao
import com.minecraftmodcreator.data.repository.ModRepositoryImpl
import com.minecraftmodcreator.domain.repository.ModRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mod_creator_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideModDao(database: AppDatabase): ModDao {
        return database.modDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindModRepository(impl: ModRepositoryImpl): ModRepository
}
