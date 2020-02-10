package com.georgeellickson.giphyviewer

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import com.georgeellickson.giphyviewer.di.AppComponent
import com.georgeellickson.giphyviewer.di.AppModule
import com.georgeellickson.giphyviewer.di.DaggerAppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

class GiphyViewerApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)

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

    fun appComponent(): AppComponent = appComponent

}

val Application.appComponent: AppComponent
    get() = (this as GiphyViewerApplication).appComponent()