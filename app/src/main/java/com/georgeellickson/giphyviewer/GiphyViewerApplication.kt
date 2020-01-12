package com.georgeellickson.giphyviewer

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import timber.log.Timber
import timber.log.Timber.DebugTree

class GiphyViewerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // init anything core, Dagger, 3rd party libs, etc.

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )

            Timber.plant(DebugTree())
        }
    }
}