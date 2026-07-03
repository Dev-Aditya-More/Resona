package com.resona.data.local.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.resona.domain.model.Song

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: String,
    val albumArtUri: String?,
    @ColumnInfo(defaultValue = "0") val isManual: Boolean = false
)

fun SongEntity.toDomain() = Song(
    id = id,
    title = title,
    artist = artist,
    album = album,
    duration = duration,
    uri = Uri.parse(uri),
    albumArtUri = albumArtUri?.let { Uri.parse(it) }
)

fun Song.toEntity(isManual: Boolean = false) = SongEntity(
    id = id,
    title = title,
    artist = artist,
    album = album,
    duration = duration,
    uri = uri.toString(),
    albumArtUri = albumArtUri?.toString(),
    isManual = isManual
)
