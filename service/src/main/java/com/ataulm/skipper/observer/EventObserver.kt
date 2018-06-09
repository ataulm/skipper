package com.ataulm.skipper.observer

import android.arch.lifecycle.Observer

class EventObserver<T>(private val handleEvent: (T) -> Unit) : Observer<Event<T>> {

    override fun onChanged(event: Event<T>?) {
        event?.value()?.let { handleEvent(it) }
    }
}
