package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private var handler: Handler = Handler()
    private var startTime: Long = 0L
    private var timeInMillis: Long = 0L
    private var timeSwapBuff: Long = 0L
    private var updateTime: Long = 0L

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMillis = SystemClock.uptimeMillis() - startTime
            updateTime = timeSwapBuff + timeInMillis
            val secs = (updateTime / 1000).toInt()
            val mins = secs / 60
            val milliseconds = (updateTime % 1000).toInt()
            tvTimer.text = String.format("%02d:%02d:%03d", mins, secs % 60, milliseconds)
            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tvTimer)
        btnStart = findViewById(R.id.btnStart)
        btnPause = findViewById(R.id.btnPause)
        btnReset = findViewById(R.id.btnReset)

        btnStart.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(updateTimerThread, 0)
            btnStart.isEnabled = false
            btnPause.isEnabled = true
            btnReset.isEnabled = true
        }

        btnPause.setOnClickListener {
            timeSwapBuff += timeInMillis
            handler.removeCallbacks(updateTimerThread)
            btnStart.isEnabled = true
            btnPause.isEnabled = false
        }

        btnReset.setOnClickListener {
            startTime = 0L
            timeInMillis = 0L
            timeSwapBuff = 0L
            updateTime = 0L
            tvTimer.text = "00:00:000"
            handler.removeCallbacks(updateTimerThread)
            btnStart.isEnabled = true
            btnPause.isEnabled = false
            btnReset.isEnabled = false
        }
    }
}
