package com.inu.roommemo.utils

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

abstract class BaseActivity : AppCompatActivity() { // 어딘가에 상속 받았을 때만 사용가능하게 abstract

    abstract fun permissionGranted(requestCode: Int)
    abstract fun permissionDenied(requestCode: Int)

    // 권한 검사
    fun requirePermissions(permissions:Array<String>, requestCode:Int) {
        // Api 버전이 마시멜로 이하이면 권한처리가 필요 없다
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else {
            // 권한이 없으면 권한 요청 -> 팝업
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }
}