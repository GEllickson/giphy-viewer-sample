package com.georgeellickson.giphyviewer.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.georgeellickson.giphyviewer.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = getString(R.string.title_settings)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

}
