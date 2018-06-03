package com.ataulm.skipper.settings

import android.util.Log

class TimeLog {
    companion object {

        private var lastTime:Long = 0

        fun start(log: String) {
            Log.d("!!!", "---")
            lastTime = System.currentTimeMillis()
            log(log)
        }

        fun log(log: String) {
            val current = System.currentTimeMillis()
            val diff = current - lastTime
            lastTime = current
            Log.d("!!!", "$log $diff")
        }
    }
}
