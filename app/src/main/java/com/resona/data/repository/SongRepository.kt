package com.resona.data.repository

import com.resona.data.local.dao.SongDao
import com.resona.data.local.entity.SongEntity
import com.resona.data.local.entity.toDomain
import com.resona.data.local.entity.toEntity
import com.resona.data.mediasource.MediaStoreScanner
import com.resona.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongRepository @Inject constructor(
    private val scanner: MediaStoreScanner,
    private val songDao: SongDao
) {
    fun getSongs(): Flow<List<Song>> = songDao.getAllSongs().map { it.map(SongEntity::toDomain) }

    suspend fun syncSongs() {
        val songs = scanner.scanSongs()
        songDao.deleteAll()
        songDao.insertAll(songs.map { it.toEntity() })
    }
}
