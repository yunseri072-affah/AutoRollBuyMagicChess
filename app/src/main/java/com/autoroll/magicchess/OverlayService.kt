package com.autoroll.magicchess

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class OverlayService : Service() {

    private var windowManager: WindowManager? = null
    private var floatingView: View? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        // Membuat layout sederhana untuk panel melayang
        val layoutParamType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutParamType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 100

        // Membuat tampilan melayang secara dinamis
        floatingView = LinearLayoutWithBackground(this)
        
        try {
            windowManager?.addView(floatingView, params)
            Toast.makeText(this, "Overlay Aktif", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Helper class untuk membuat tampilan widget melayang secara instan tanpa XML tambahan
    private fun LinearLayoutWithBackground(context: android.content.Context): View {
        val layout = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setBackgroundColor(android.graphics.Color.parseColor("#CC000000")) // Hitam transparan
            setPadding(24, 24, 24, 24)
        }

        val tvTitle = TextView(context).apply {
            text = "Magic Chess Auto"
            setTextColor(android.graphics.Color.WHITE)
            textSize = 14f
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val btnClose = Button(context).apply {
            text = "Tutup Overlay"
            textSize = 12f
            setOnClickListener {
                stopSelf()
            }
        }

        layout.addView(tvTitle)
        layout.addView(btnClose)
        return layout
    }

    override fun onDestroy() {
        super.onDestroy()
        if (floatingView != null) {
            windowManager?.removeView(floatingView)
            floatingView = null
        }
        Toast.makeText(this, "Overlay Ditutup", Toast.LENGTH_SHORT).show()
    }
}
