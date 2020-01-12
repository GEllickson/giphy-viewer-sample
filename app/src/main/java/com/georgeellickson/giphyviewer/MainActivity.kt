package com.georgeellickson.giphyviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.georgeellickson.giphyviewer.home.HomeFragment
import com.georgeellickson.giphyviewer.settings.SettingsFragment
import com.georgeellickson.giphyviewer.storage.GiphyKeyPref
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: GiphyKeyPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        application.appComponent.inject(this)

        if (savedInstanceState == null) {
            val startingFragment: Fragment =
                if (prefs.hasApiKey()) {
                    HomeFragment()
                } else {
                    SettingsFragment()
                }
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, startingFragment).commit()
        }
    }
}
