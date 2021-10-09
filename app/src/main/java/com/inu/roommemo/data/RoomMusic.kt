package com.inu.roommemo.data

import android.net.Uri
import android.provider.MediaStore
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_memo")
class RoomMusic { //(id: String, title:String?, artist:String?, albumId:String?, duration:Long?) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var no:Long? = null

    @ColumnInfo
    var id: String = "" // 음원 자체의 id
    @ColumnInfo
    var title: String? = ""
    @ColumnInfo
    var artist : String? = ""
    @ColumnInfo
    var albumId: String?     = ""  // 앨범이미지  id
    @ColumnInfo
    var duration : Long? = 0

    /*
    init {
        this.id = id
        this.title = title
        this.artist = artist
        this.albumId = albumId
        this.duration = duration
    } */

    constructor(id: String, title: String, artist: String, albumId: String, duration: Long) {
        this.id = id
        this.title = title
        this.artist =  artist
        this.albumId = albumId
        this.duration = duration
    }

    fun getMusicUri(): Uri {
        return Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
    }

    fun  getAlbumUri(): Uri {
        return Uri.parse("content://media/external/audio/albumart/$albumId")
    }
}