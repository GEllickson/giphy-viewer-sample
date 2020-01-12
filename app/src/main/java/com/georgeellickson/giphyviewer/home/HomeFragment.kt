package com.georgeellickson.giphyviewer.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.georgeellickson.giphyviewer.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = getString(R.string.title_home)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}
