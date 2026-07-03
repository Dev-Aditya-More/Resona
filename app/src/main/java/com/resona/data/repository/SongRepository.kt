package com.resona.data.repository

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.resona.data.local.dao.SongDao
import com.resona.data.local.entity.SongEntity
import com.resona.data.local.entity.toDomain
import com.resona.data.local.entity.toEntity
import com.resona.data.mediasource.MediaStoreScanner
import com.resona.domain.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scanner: MediaStoreScanner,
    private val songDao: SongDao
) {
    fun getSongs(): Flow<List<Song>> = songDao.getAllSongs().map { it.map(SongEntity::toDomain) }

    suspend fun syncSongs() {
        val songs = scanner.scanSongs()
        songDao.deleteAutoSongs()
        songDao.insertAll(songs.map { it.toEntity() })
    }

    suspend fun addManualSongs(uris: List<Uri>): ManualImportResult = withContext(Dispatchers.IO) {
        val existing = songDao.getAllSongsOnce()
        val existingUris = existing.mapTo(mutableSetOf()) { it.uri }
        val seenFingerprints = existing.mapTo(mutableSetOf()) { it.fingerprint() }

        var duplicates = 0
        val songs = uris.mapNotNull { uri ->
            if (uri.toString() in existingUris) {
                duplicates++
                return@mapNotNull null
            }
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                extractMetadata(uri)
            }.getOrNull()
        }.filter { song ->
            // Catches the same track already present under a different URI (e.g. picked
            // manually after being found by the MediaStore scan).
            val isDuplicate = !seenFingerprints.add(song.fingerprint())
            if (isDuplicate) duplicates++
            !isDuplicate
        }

        if (songs.isNotEmpty()) {
            songDao.insertAll(songs.map { it.toEntity(isManual = true) })
        }
        ManualImportResult(added = songs.size, duplicates = duplicates)
    }

    private fun extractMetadata(uri: Uri): Song {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, uri)
            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                ?: uri.lastPathSegment?.substringBeforeLast('.') ?: "Unknown"
            val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown Artist"
            val album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: "Unknown Album"
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
            // Generate a stable ID from the URI, offset to avoid MediaStore ID collisions
            val id = (uri.toString().hashCode().toLong() and 0x7FFF_FFFFL) + 1_000_000_000L
            return Song(
                id = id,
                title = title,
                artist = artist,
                album = album,
                duration = duration,
                uri = uri,
                albumArtUri = null
            )
        } finally {
            retriever.release()
        }
    }
}

data class ManualImportResult(val added: Int, val duplicates: Int)

private fun Song.fingerprint() = "${title.trim().lowercase()}|${artist.trim().lowercase()}|$duration"
private fun SongEntity.fingerprint() = "${title.trim().lowercase()}|${artist.trim().lowercase()}|$duration"
