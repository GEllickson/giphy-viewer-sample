package com.georgeellickson.giphyviewer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.georgeellickson.giphyviewer.home.HomeFragment
import com.georgeellickson.giphyviewer.settings.SettingsFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main), NavController {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.appComponent.inject(this)

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
            .replace(android.R.id.content, fragment).commit()
    }
}
