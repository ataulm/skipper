package com.ataulm.skipper.settings

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ataulm.skipper.SkipperRepository

class SkipperViewModelProvider(private val repository: SkipperRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SkipperSettingsViewModel(repository) as T
    }
}
