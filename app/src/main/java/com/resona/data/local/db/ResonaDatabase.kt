package com.resona.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.resona.data.local.dao.SongDao
import com.resona.data.local.entity.SongEntity

@Database(entities = [SongEntity::class], version = 1, exportSchema = false)
abstract class ResonaDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}
