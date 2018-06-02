package com.example

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Watches for window changes to scan for a "Continue watching" button to click it for you.
 */
class ContinueWatchingService : AccessibilityService() {

    private lateinit var clickableWordsSharedPrefs: ClickableWordsSharedPrefs
    private var clickableWords = emptyList<ClickableWord>()

    override fun onCreate() {
        super.onCreate()
        clickableWordsSharedPrefs = ClickableWordsSharedPrefs.create(this)
        clickableWordsSharedPrefs.addOnChangeListener(callback)
        clickableWords = clickableWordsSharedPrefs.clickableWords()
    }

    override fun onDestroy() {
        clickableWordsSharedPrefs.removeChangeListener(callback)
        super.onDestroy()
    }

    private val callback = object : ClickableWordsSharedPrefs.Callback {

        override fun onChange(clickableWords: List<ClickableWord>) {
            this@ContinueWatchingService.clickableWords = clickableWords
        }
    }

    override fun onInterrupt() {
        // no op - this has no feedback so there'll be nothing to interrupt
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        clickableWords.forEach({ clickWordIfPresent(event, it.word) })
    }

    private fun clickWordIfPresent(event: AccessibilityEvent?, clickableWord: String) {
        val list = event?.source?.findAccessibilityNodeInfosByText(clickableWord).orEmpty()
        if (list.isEmpty()) {
            return
        }
        list.first()?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }
}