package com.ataulm.skipper.observer

open class Event<T>(private val value: T) {

    private var delivered = false

    fun value(): T? {
        return if (delivered) {
            null
        } else {
            delivered = true
            value
        }
    }
}
