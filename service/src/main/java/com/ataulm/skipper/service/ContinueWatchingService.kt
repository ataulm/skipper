package com.ataulm.skipper.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.ataulm.skipper.ClickableWord
import com.ataulm.skipper.SkipperSharedPrefs

/**
 * Watches for window changes to scan for a "Continue watching" button to click it for you.
 */
class ContinueWatchingService : AccessibilityService() {

    // TODO: ContinueWatchingRepository, rather than direct access
    private lateinit var skipperSharedPrefs: SkipperSharedPrefs
    private var clickableWords = emptyList<ClickableWord>()

    override fun onCreate() {
        super.onCreate()
        skipperSharedPrefs = SkipperSharedPrefs.create(this)
        skipperSharedPrefs.addOnChangeListener(callback)
        clickableWords = skipperSharedPrefs.clickableWords()
    }

    override fun onDestroy() {
        skipperSharedPrefs.removeChangeListener(callback)
        super.onDestroy()
    }

    private val callback = object : SkipperSharedPrefs.Callback {

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
        if (list.isNotEmpty()) {
            val info = list.first()
            info.clickClosestAncestor()
        }
    }

    private fun AccessibilityNodeInfo?.clickClosestAncestor() {
        if (this == null) {
            return
        }

        if (hasClickAction()) {
            performAction(AccessibilityNodeInfo.ACTION_CLICK)
        } else {
            parent.clickClosestAncestor()
        }
    }

    private fun AccessibilityNodeInfo.hasClickAction(): Boolean {
        actionList?.forEach({
            if (it.id == AccessibilityNodeInfo.ACTION_CLICK) {
                return true
            }
        })
        return false
    }
}
