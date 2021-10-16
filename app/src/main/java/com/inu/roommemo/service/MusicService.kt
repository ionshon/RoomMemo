package com.inu.roommemo.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class MusicService : Service() {

    override fun onBind(intent: Intent): IBinder {

        return MyBinder()
    }
    inner class MyBinder : Binder(){
        val instance: MusicService
            get() = this@MusicService
    }

}