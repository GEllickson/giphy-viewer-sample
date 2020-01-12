package com.georgeellickson.giphyviewer.di

import com.georgeellickson.giphyviewer.MainActivity
import com.georgeellickson.giphyviewer.settings.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: SettingsFragment)

}