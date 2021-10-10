package com.inu.roommemo.utils

import android.os.Environment
import java.io.File
import java.io.IOException

class albumthum1 {

    fun a(): File? {
        val file = File(Environment.getExternalStorageDirectory(), "/albumthumbs/")
        if (!file.exists()) {
            file.mkdirs()
            try {
                File(file, ".nomedia").createNewFile()
            } catch (e2: IOException) {
                e2.printStackTrace()
            }
        }
        return file
    }

}