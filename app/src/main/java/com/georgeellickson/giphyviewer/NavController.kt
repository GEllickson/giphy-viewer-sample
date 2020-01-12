package com.georgeellickson.giphyviewer

import android.app.Activity
import androidx.fragment.app.Fragment

interface NavController {
    fun navigateTo(fragment: Fragment)
}

val Activity.navController: NavController
    get() = this as NavController