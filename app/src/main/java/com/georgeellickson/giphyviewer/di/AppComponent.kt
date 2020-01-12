package com.georgeellickson.giphyviewer.di

import com.georgeellickson.giphyviewer.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

}