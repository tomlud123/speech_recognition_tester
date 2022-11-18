package org.vosk.demo

import android.app.Activity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import kotlinx.android.synthetic.main.activity_google_sr.*

class GoogleSrActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sr)

        setUiState(SrActivityState.STATE_START)

        //TODO: init model
    }

    override fun onDestroy() {
        super.onDestroy()
        //TODO implement
    }

    fun clickFile(v: View){
        //TODO
    }

    fun clickMic(v: View){
        //TODO
    }

    fun clickPause(v: View){
        //TODO
    }

    private fun setUiState(state: SrActivityState) {
        when (state) {
            SrActivityState.STATE_START -> {
                result_text.setText(R.string.preparing)
                result_text.movementMethod = ScrollingMovementMethod()
                btnFile.isEnabled = false
                btnMic.isEnabled = false
                btnPause.isEnabled = false
            }
            SrActivityState.STATE_READY -> {
                result_text.setText(R.string.ready)
                btnMic.setText(R.string.recognize_microphone)
                btnFile.isEnabled = true
                btnMic.isEnabled = true
                btnPause.isEnabled = false
            }
            SrActivityState.STATE_DONE -> {
                (btnFile).setText(R.string.recognize_file)
                (btnMic).setText(R.string.recognize_microphone)
                btnFile.isEnabled = true
                btnMic.isEnabled = true
                btnPause.isEnabled = false
            }
            SrActivityState.STATE_FILE -> {
                (btnFile).setText(R.string.stop_file)
                result_text.text = getString(R.string.starting)
                btnMic.isEnabled = false
                btnFile.isEnabled = true
                btnPause.isEnabled = false
            }
            SrActivityState.STATE_MIC -> {
                (btnMic).setText(R.string.stop_microphone)
                result_text.text = getString(R.string.say_something)
                btnFile.isEnabled = false
                btnMic.isEnabled = true
                btnPause.isEnabled = true
            }
        }
    }


}