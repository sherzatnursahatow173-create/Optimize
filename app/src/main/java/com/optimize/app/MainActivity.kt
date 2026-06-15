package com.optimize.app

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.widget.*

class MainActivity : AppCompatActivity() {
    private lateinit var ramText: TextView
    private lateinit var optimizeBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ramText = findViewById(R.id.ramText)
        optimizeBtn = findViewById(R.id.optimizeBtn)
        progressBar = findViewById(R.id.progressBar)
        statusText = findViewById(R.id.statusText)
        startMonitoring()
        optimizeBtn.setOnClickListener { optimizeSystem() }
    }

    private fun startMonitoring() {
        handler.post(object : Runnable {
            override fun run() {
                updateStats()
                handler.postDelayed(this, 2000)
            }
        })
    }

    private fun updateStats() {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(memInfo)
        val totalMB = memInfo.totalMem / 1048576
        val availMB = memInfo.availMem / 1048576
        val usedMB = totalMB - availMB
        val ramPercent = (usedMB * 100 / totalMB).toInt()
        ramText.text = "RAM: ${usedMB}MB / ${totalMB}MB (%${ramPercent})"
        progressBar.progress = ramPercent
    }

    private fun optimizeSystem() {
        statusText.text = "Optimize ediliyor..."
        optimizeBtn.isEnabled = false
        Runtime.getRuntime().gc()
        handler.postDelayed({
            updateStats()
            statusText.text = "Optimizasyon tamamlandi!"
            optimizeBtn.isEnabled = true
        }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
