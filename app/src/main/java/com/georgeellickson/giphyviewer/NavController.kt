package com.georgeellickson.giphyviewer

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment

interface NavController {
    fun navigateTo(fragment: Fragment, backStack: Boolean)

    fun navigateTo(fragment: Fragment, sharedElement: View)
}

val Activity.navController: NavController
    get() = this as NavController