package com.inu.roommemo.adapter

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inu.roommemo.databinding.MusicItemLayoutBinding
import com.inu.roommemo.data.RoomMusic
import java.text.SimpleDateFormat

class MusicAdapter(val musicList: MutableList<RoomMusic>) : // (private val onClick: (Music) -> Unit):
    RecyclerView.Adapter<MusicAdapter.Holder>() {

  //  var helper: RoomMusicHelper? = null

    var mediaPlayer:MediaPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       // val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        val binding = MusicItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val music = musicList[position]
        holder.setMusic(music)
    }

    inner class Holder(val binding: MusicItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        var musicUri: Uri? = null

        init {
            binding.root.setOnClickListener {
                if(mediaPlayer != null) {
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
                mediaPlayer = MediaPlayer.create(binding.root.context, musicUri)
                mediaPlayer?.start()
            }
        }
        fun setMusic(music:RoomMusic) {
            //var albumurl = music.getAlbumUri()
             //Log.d("앨범Uri:", "${music.getAlbumUri()}")
            with(binding) {
                imageAlbum.setImageURI(music.getAlbumUri())
                texArtist.text = music.artist
                textTitle.text = music.title
                val sdf = SimpleDateFormat("mm:ss")
                textDuration.text = sdf.format(music.duration)
            }
            this.musicUri = music.getMusicUri()
        }
    }
}
