package com.resona.di

import android.content.Context
import androidx.room.Room
import com.resona.data.local.dao.SongDao
import com.resona.data.local.db.ResonaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ResonaDatabase =
        Room.databaseBuilder(context, ResonaDatabase::class.java, "resona.db").build()

    @Provides
    fun provideSongDao(db: ResonaDatabase): SongDao = db.songDao()
}
