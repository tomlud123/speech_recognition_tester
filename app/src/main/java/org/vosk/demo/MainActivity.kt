package org.vosk.demo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun googleSRClick(v: View){
        startActivity(Intent(this, GoogleSrActivity::class.java))
    }
    fun voskClick(v: View){
        startActivity(Intent(this, VoskActivity::class.java))
    }
}