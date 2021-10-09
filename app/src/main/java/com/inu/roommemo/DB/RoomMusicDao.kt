package com.inu.roommemo.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.inu.roommemo.data.RoomMusic

// 입출력 접근
@Dao
interface RoomMusicDao {
    @Query("select * from music_memo")
    fun getAll(): List<RoomMusic>

    @Insert(onConflict = REPLACE)
    fun  insert(memo: RoomMusic)

    @Delete
    fun delete(memo: RoomMusic)
}