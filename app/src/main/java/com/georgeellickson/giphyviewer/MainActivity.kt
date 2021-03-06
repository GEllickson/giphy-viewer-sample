package com.georgeellickson.giphyviewer

import android.os.Bundle
import android.view.View
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
            navigateTo(fragment, false)
        }
    }

    override fun navigateTo(fragment: Fragment, backStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
            .replace(android.R.id.content, fragment)
        if (backStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun navigateTo(fragment: Fragment, sharedElement: View) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(sharedElement, sharedElement.transitionName)
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }


}
