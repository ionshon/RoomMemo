package com.inu.roommemo

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.inu.roommemo.DB.RoomMusicHelper
import com.inu.roommemo.DB.RoomMusicDao
import com.inu.roommemo.adapter.MusicAdapter
import com.inu.roommemo.data.RoomMusic
import com.inu.roommemo.databinding.ActivityMainBinding
import com.inu.roommemo.databinding.ControlsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    val bindingControl by lazy { ControlsBinding.inflate(layoutInflater)  }

    val permission = Manifest.permission.READ_EXTERNAL_STORAGE
    val REQ_READ = 99

    val musicLists = mutableListOf<RoomMusic>()

    var i = 0

    lateinit var helper: RoomMusicHelper // Room 데이터베이스 초기화
    lateinit var musicAdapter: MusicAdapter // 어댑터 객체 생성 연결
    lateinit var musicDAO: RoomMusicDao // DAO 객체 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        if(isPermitted()) {
            startProcess()
        }else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQ_READ)
        }

        bindingControl.buttonPlayPause.setOnClickListener {
            musicPlay()
        }
    }

    fun musicPlay() {
        var mediaPlayer:MediaPlayer? = null
        if(mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
        mediaPlayer = MediaPlayer.create(binding.root.context, R.raw.pop_pop_pop)
        mediaPlayer?.start()

    }


    fun startProcess() {
        helper = Room.databaseBuilder(this, RoomMusicHelper::class.java, "room_music")
            .addMigrations(MigrateDatabase.MIGRATE_1_2)    //   .allowMainThreadQueries() // 공부할 때만 쓴다
            .build()
        musicDAO = helper.roomMusicDao()
        musicAdapter = MusicAdapter(musicLists)

        binding.progress.visibility = View.VISIBLE
        insertMusicList()
        setTitle("곡수 : $i")
        CoroutineScope(Dispatchers.IO).launch {
            musicAdapter.musicList.clear()
            musicAdapter.musicList.addAll(musicDAO.getAll())

            withContext(Dispatchers.Main) { // 화면을 갱신할 때만 메인 쓰레드를 실행해
            //    musicAdapter.notifyDataSetChanged()  // 추가시 사용
                with(binding) {
                    Log.d("코루틴 빡 : ", "$i")
                    recyclerMemo.adapter = musicAdapter
                    recyclerMemo.layoutManager = LinearLayoutManager(this@MainActivity)
                    progress.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun insertMusicList() {
        val musicListUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        // 2. 가져올 데이터 컬컴 정의
        val proj = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )
        //3.  컨텐트 리졸버에 해당 데이터 요청
        val cursor = contentResolver.query(musicListUri, proj, null, null, null)
        // 4. 커서로 전달받은 데이터를 꺼내서 저장
        //    val musicList = mutableListOf<RoomMusic>()
        while (cursor?.moveToNext() == true) {
            i += 1
            val id = cursor.getString(0)
            val title = cursor.getString(1)
            val artist = cursor.getString(2)
            val albumId = cursor.getString(3)
            val duration = cursor.getLong(4)

            val music = RoomMusic(id, title, artist, albumId, duration)

            CoroutineScope(Dispatchers.IO).launch {
                musicDAO.insert(music) // musicList.add(music)
            }
        }
        cursor?.close()
    }


    fun isPermitted() : Boolean { // 책에는 checkPermission, 조건이 하나일 때 한줄로
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQ_READ) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startProcess()
            } else {
                Toast.makeText(this, "권한 요청 실행해야지 앱 실행", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}


//룸 변경사항 적용하기
object MigrateDatabase {
    val MIGRATE_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val alter = "ALTER table room_memo add column new_title text"
            database.execSQL(alter)
        }
    }
}