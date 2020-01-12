package com.georgeellickson.giphyviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.georgeellickson.giphyviewer.storage.GiphyKeyPref
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: GiphyKeyPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        application.appComponent.inject(this)
    }
}
