package com.georgeellickson.giphyviewer.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.georgeellickson.giphyviewer.R

class ViewImageFragment : Fragment(R.layout.fragment_view_image) {

    private var isLeaving = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnClickListener {
            if (!isLeaving) {
                isLeaving = true
                requireActivity().onBackPressed()
            }
        }
        val gifView = view.findViewById<ImageView>(R.id.image_gif)
        val gifUrl = arguments?.getString(EXTRA_URL) ?: ""
        Glide.with(view)
            .asGif()
            .load(gifUrl)
            .into(gifView)
    }

    companion object {
        private const val EXTRA_URL = "image_url"
        fun newInstance(url: String): Fragment {
            return ViewImageFragment().apply {
                arguments = Bundle().apply { putString(EXTRA_URL, url) }
            }
        }
    }
}