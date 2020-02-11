package com.georgeellickson.giphyviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.georgeellickson.giphyviewer.home.HomeFragment
import com.georgeellickson.giphyviewer.settings.SettingsFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main), NavController {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        application.appComponent.inject(this)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment: Fragment = when (viewModel.getStartState()) {
                LaunchStartState.HOME -> HomeFragment()
                LaunchStartState.SETTINGS -> SettingsFragment()
            }
            navigateTo(fragment)
        }
    }

    override fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
            .replace(android.R.id.content, fragment)
            .commit()
    }
}
