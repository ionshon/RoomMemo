package com.inu.roommemo.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inu.roommemo.data.RoomMusic

@Database(entities = arrayOf(RoomMusic::class), version = 1, exportSchema = false)
abstract class RoomMusicHelper: RoomDatabase() {
    abstract fun roomMusicDao(): RoomMusicDao

}