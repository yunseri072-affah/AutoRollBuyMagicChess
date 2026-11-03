package com.autoroll.magicchess

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvLog: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnPermissions: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        tvLog = findViewById(R.id.tvLog)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        btnPermissions = findViewById(R.id.btnPermissions)

        btnPermissions.setOnClickListener {
            requestPermissions()
        }

        btnStart.setOnClickListener {
            if (checkSystemPermissions()) {
                startAutomationServices()
            } else {
                Toast.kt(this, "Harap berikan izin terlebih dahulu!", Toast.LENGTH_SHORT).show()
                appendLog("Gagal Start: Izin Accessibility atau Overlay belum aktif.")
            }
        }

        btnStop.setOnClickListener {
            stopAutomationServices()
        }
        
        appendLog("Aplikasi siap digunakan. Silakan berikan izin sistem.")
    }

    private fun requestPermissions() {
        // 1. Izin Overlay (Display over other apps)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }

        // 2. Izin Accessibility Service
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
        
        appendLog("Mengarahkan ke pengaturan izin...")
    }

    private fun checkSystemPermissions(): Boolean {
        val overlayChecked = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)
        // Catatan: Pengecekan accessibility sederhana lewat kesiapan service
        return overlayChecked
    }

    private fun startAutomationServices() {
        // Mulai Floating Overlay Service
        val overlayIntent = Intent(this, OverlayService::class.java)
        startService(overlayIntent)

        tvStatus.text = "Status: Berjalan (Running)"
        tvStatus.setTextColor(android.graphics.Color.parseColor("#00E676"))
        appendLog("Otomatisasi Magic Chess dimulai!")
        Toast.makeText(this, "AutoRollBuy Dimulai", Toast.LENGTH_SHORT).show()
    }

    private fun stopAutomationServices() {
        // Hentikan Overlay Service
        val overlayIntent = Intent(this, OverlayService::class.java)
        stopService(overlayIntent)

        tvStatus.text = "Status: Nonaktif"
        tvStatus.setTextColor(android.graphics.Color.parseColor("#FF5252"))
        appendLog("Otomatisasi dihentikan.")
        Toast.makeText(this, "AutoRollBuy Dihentikan", Toast.LENGTH_SHORT).show()
    }

    private fun appendLog(message: String) {
        val currentText = tvLog.text.toString()
        tvLog.text = "$currentText\n> $message"
    }
}
