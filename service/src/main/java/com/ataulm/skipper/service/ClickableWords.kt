package com.ataulm.skipper.service

import com.ataulm.skipper.AppPackageName

data class ClickableWords(private val data: Map<String, Set<String>>) {

    fun clickableWordsFor(app: AppPackageName):Set<String> {
        return data[app.packageName].orEmpty()
    }
}
