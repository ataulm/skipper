package com.ataulm.skipper.edit

import android.arch.lifecycle.LiveData
import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.ClickableWord
import com.ataulm.skipper.SkipperRepository

class EditAppAssociationsViewModel(private val appPackageName: AppPackageName, private val repository: SkipperRepository) {

    fun clickableWords(): LiveData<List<ClickableWord>> {
        val clickableWords = repository.clickableWords(appPackageName)
        return clickableWords
    }

    fun onClickAdd(word: ClickableWord) {
        repository.add(appPackageName, word)
    }

    fun onClickDelete(word: ClickableWord) {
        repository.delete(appPackageName, word)
    }

    fun onClickDismiss() {
        // TODO: send close event
    }
}
