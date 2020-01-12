package com.georgeellickson.giphyviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.georgeellickson.giphyviewer.home.HomeFragment
import com.georgeellickson.giphyviewer.settings.SettingsFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavController {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        application.appComponent.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

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
