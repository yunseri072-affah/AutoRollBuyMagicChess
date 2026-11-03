package com.autoroll.magicchess

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class AutoService : AccessibilityService() {

    companion object {
        var instance: AutoService? = null
            private set
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Tempat logika interaksi otomatis (Auto Roll, Auto Buy, Auto Sell)
    }

    override fun onInterrupt() {
        instance = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
