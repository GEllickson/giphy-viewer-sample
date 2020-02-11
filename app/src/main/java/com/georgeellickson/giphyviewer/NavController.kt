package com.georgeellickson.giphyviewer

import android.app.Activity
import androidx.fragment.app.Fragment

interface NavController {
    fun navigateTo(fragment: Fragment, backStack: Boolean)
}

val Activity.navController: NavController
    get() = this as NavController