package com.ataulm.skipper

import android.app.Application
import android.content.Context

class SkipperApplication : Application() {

    private lateinit var persistence: SkipperPersistence

    override fun onCreate() {
        super.onCreate()
        persistence = SharedPreferencesSkipperPersistence.create(this)
    }

    fun injectPersistence(): SkipperPersistence {
        return persistence
    }
}

fun Context.injectPersistence(): SkipperPersistence {
    return (applicationContext as SkipperApplication).injectPersistence()
}
