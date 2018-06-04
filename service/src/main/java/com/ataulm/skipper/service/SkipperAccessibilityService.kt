package com.ataulm.skipper.service

import android.accessibilityservice.AccessibilityService
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.SharedPreferencesSkipperPersistence

class SkipperAccessibilityService : AccessibilityService() {

    private lateinit var liveData: LiveData<AppsToWordsMap>

    private var appsToWordsMap: AppsToWordsMap = emptyMap()

    override fun onCreate() {
        super.onCreate()
        liveData = SkipperRepository(SharedPreferencesSkipperPersistence.create(this)).appsToWordsMap()
        liveData.observeForever(observer)
    }

    override fun onDestroy() {
        liveData.removeObserver(observer)
        super.onDestroy()
    }

    private val observer = Observer<AppsToWordsMap> { appsToWordsMap ->
        if (appsToWordsMap == null) {
            return@Observer
        }
        this@SkipperAccessibilityService.appsToWordsMap = appsToWordsMap
    }

    override fun onInterrupt() {
        // no op - this has no feedback so there'll be nothing to interrupt
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val clickableWords = appsToWordsMap[AppPackageName(event.packageName.toString())].orEmpty()
        clickableWords.forEach {
            val matchingNodes = event.source?.findAccessibilityNodeInfosByText(it.word).orEmpty()
            matchingNodes.forEach { node ->
                // we want at most one successful click from any of the nodes, matching any of the words
                if (node.clickClosestAncestor()) {
                    return
                }
            }
        }
    }

    /**
     * @return true if something was clicked
     */
    private fun AccessibilityNodeInfo?.clickClosestAncestor(): Boolean {
        if (this == null) {
            return false
        }

        if (hasClickAction()) {
            performAction(AccessibilityNodeInfo.ACTION_CLICK)
            return true
        } else {
            return parent.clickClosestAncestor()
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
