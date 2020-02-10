package com.georgeellickson.giphyviewer.di

import android.content.Context
import com.georgeellickson.giphyviewer.MainActivity
import com.georgeellickson.giphyviewer.home.HomeFragment
import com.georgeellickson.giphyviewer.settings.SettingsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)

    fun inject(fragment: SettingsFragment)

    fun inject(fragment: HomeFragment)

}